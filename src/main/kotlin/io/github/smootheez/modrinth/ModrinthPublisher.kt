// ModrinthPublisher: Publishes mod artifacts to Modrinth via API
package io.github.smootheez.modrinth

import io.github.smootheez.McModPublisherExtension
import io.github.smootheez.PublisherConfig
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.gradle.api.Project
import java.io.File

// API endpoints used for publishing and retrieving game versions
private const val UPLOAD_URL = "https://api.modrinth.com/v2/version"
private const val GAME_VERSION_URL = "https://api.modrinth.com/v2/tag/game_version"

// Data class used to deserialize game version data from Modrinth
@Serializable
private data class GameVersionTag(
    val version: String,
    @SerialName("version_type") val versionType: String,
    val date: String,
    val major: Boolean
)

class ModrinthPublisher(
    private val project: Project,
    private val extension: McModPublisherExtension,
    private val client: OkHttpClient = OkHttpClient()
) {
    // JSON parser with unknown field tolerance
    private val json = Json { ignoreUnknownKeys = true }

    fun publish() {
        val modrinth = extension.modrinth
        val token = modrinth.token?.trim()
        val projectId = modrinth.projectId?.trim()
        val files = extension.files.files

        project.logger.lifecycle("🚀  Publishing to Modrinth...")

        // ✅ Validate required fields
        val missing = buildList {
            if (token.isNullOrBlank()) add("Modrinth token")
            if (projectId.isNullOrBlank()) add("Project ID")
            if (files.isEmpty()) add("Files to upload")
        }

        if (missing.isNotEmpty()) {
            project.logger.error("❌ Missing required configuration: ${missing.joinToString(", ")}. Aborting.")
            return
        }

        project.logger.lifecycle("✅ Found ${files.size} file(s) for upload.")
        project.logger.lifecycle("✅ All required configuration is set.")

        // Fetch allowed Minecraft game versions from Modrinth API
        project.logger.lifecycle("⏳ Fetching valid game versions from Modrinth...")
        val validGameVersions = fetchValidGameVersions()
        if (validGameVersions.isEmpty()) {
            project.logger.error("❌ Failed to fetch valid game versions from Modrinth. Aborting.")
            return
        }
        project.logger.lifecycle("✅ Retrieved ${validGameVersions.size} valid game versions.")

        // Validate user configured game versions
        val userGameVersions = extension.gameVersions.map { it.trim() }
        val invalidVersions = userGameVersions.filterNot { it in validGameVersions }

        if (invalidVersions.isNotEmpty()) {
            project.logger.error("❌ Invalid Minecraft version(s): ${invalidVersions.joinToString()}")
            project.logger.error("  ➜ Check available versions on Modrinth or update your configuration.")
            return
        }
        project.logger.lifecycle("✅ Game versions validated: ${userGameVersions.joinToString()}")

        // Ensure release type is valid (release / beta / alpha)
        val releaseType = extension.releaseType.lowercase()
        if (releaseType !in PublisherConfig.VALID_RELEASE_TYPES) {
            project.logger.error("❌ Invalid release type: '$releaseType'")
            project.logger.error("  ➜ Allowed values: release, beta, alpha.")
            return
        }
        project.logger.lifecycle("✅ Release type validated: $releaseType")

        // Build mod metadata payload
        project.logger.lifecycle("⏳ Preparing metadata...")

        val dependencyList = modrinth.dependencies.asMap.values.map {
            DependencyMetadata(
                projectId = it.projectId,
                dependencyType = it.dependencyType
            )
        }

        // Match files to multipart field names
        val filePartNames = files.mapIndexed { index, _ ->
            if (index == 0) "file" else "file_$index"
        }

        // Metadata sent as JSON in the multipart request
        val metadata = ModrinthMetadata(
            projectId = projectId!!,
            name = extension.displayName,
            versionNumber = extension.version ?: project.version.toString(),
            changelog = extension.changelog,
            gameVersions = userGameVersions,
            loaders = extension.loaders,
            releaseChannel = releaseType,
            featured = modrinth.featured,
            status = modrinth.status,
            dependencies = dependencyList,
            fileParts = filePartNames
        )

        val jsonMetadata = json.encodeToString(metadata)
        project.logger.lifecycle("📦 Metadata prepared: $jsonMetadata")

        project.logger.lifecycle("⏳ Uploading to Modrinth...")

        publishingToModrinth(jsonMetadata, files, filePartNames, token!!)
    }

    private fun publishingToModrinth(
        jsonMetadata: String,
        files: Set<File>,
        filePartNames: List<String>,
        token: String
    ) {
        val multipartBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
        multipartBuilder.addFormDataPart(
            "data",
            null,
            jsonMetadata.toRequestBody(PublisherConfig.MEDIA_TYPE_JSON.toMediaType())
        )

        // Attach all files to the multipart request
        files.forEachIndexed { index, file ->
            val partName = filePartNames[index]
            project.logger.lifecycle("📁 Adding file part: $partName (${file.name})")
            multipartBuilder.addFormDataPart(
                partName,
                file.name,
                file.asRequestBody(PublisherConfig.MEDIA_TYPE_JAR.toMediaType())
            )
        }

        try {
            val requestBody = multipartBuilder.build()
            val request = Request.Builder()
                .url(UPLOAD_URL)
                .addHeader("User-Agent", PublisherConfig.USER_AGENT)
                .addHeader("Authorization", token)
                .post(requestBody)
                .build()

            // Execute the request and handle result
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val body = response.body.string()
                    project.logger.error("❌ Upload failed: ${response.code} ${response.message}")
                    if (body.isNotBlank()) {
                        project.logger.error("Response body: $body")
                    }
                } else {
                    project.logger.lifecycle("✅ Successfully uploaded to Modrinth!")
                    val responseBody = response.body.string()
                    if (responseBody.isNotBlank()) {
                        project.logger.lifecycle("📨 Response: $responseBody")
                    }
                }
            }
        } catch (e: Exception) {
            project.logger.error("❌ Upload to Modrinth failed due to network or I/O error.")
            project.logger.error("   ➜ ${e::class.simpleName}: ${e.message}")
        }
    }

    private fun fetchValidGameVersions(): Set<String> {
        return try {
            val request = Request.Builder()
                .url(GAME_VERSION_URL)
                .addHeader("User-Agent", PublisherConfig.USER_AGENT)
                .build()

            // Request valid game versions and parse JSON response
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return emptySet()
                }

                val body = response.body.string()
                if (body.isBlank()) return emptySet()

                val parsed = json.decodeFromString<List<GameVersionTag>>(body)
                parsed.map { it.version }.toSet()
            }
        } catch (e: Exception) {
            project.logger.error("❌ Error fetching game versions: ${e.message}")
            emptySet()
        }
    }
}