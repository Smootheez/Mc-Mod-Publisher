package io.github.smootheez.curseforge

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

// Base API endpoints for CurseForge upload and version information
private const val UPLOAD_URL = "https://minecraft.curseforge.com/api/projects/%s/upload-file"
private const val GAME_VERSIONS_URL = "https://minecraft.curseforge.com/api/api/game/versions"

// Represents the JSON structure for CurseForge game version data
@Serializable
private data class GameVersionTag(
    val id: Int,
    @SerialName("gameVersionTypeID") val gameVersionTypeId: Int,
    val name: String,
    val slug: String
)

class CurseforgePublisher(
    private val project: Project,
    private val extension: McModPublisherExtension,
    private val client: OkHttpClient = OkHttpClient()
) {
    /**
     * Main entry point to publish the mod to CurseForge.
     */
    fun publish() {
        val curseforge = extension.curseforge
        val token = curseforge.token?.trim()
        val projectId = curseforge.projectId?.trim()
        val files = extension.files.files

        project.logger.lifecycle("🚀  Publishing to CurseForge...")

        // Validate required configuration fields
        val missing = buildList {
            if (token.isNullOrBlank()) add("CurseForge token")
            if (projectId.isNullOrBlank()) add("Project ID")
            if (files.isEmpty()) add("File to upload (CurseForge allows only one)")
        }

        // Abort if configuration is incomplete
        if (missing.isNotEmpty()) {
            project.logger.error("❌ Missing required configuration: ${missing.joinToString(", ")}. Aborting.")
            return
        }

        project.logger.lifecycle("✅ Found ${files.size} file(s) for upload.")
        project.logger.lifecycle("✅ All required configuration is set.")

        // Get list of currently allowed Minecraft game versions from CurseForge API
        project.logger.lifecycle("⏳ Fetching valid game versions from CurseForge...")
        val validVersions = fetchValidGameVersions(token!!)
        val userVersions = extension.gameVersions.map { it.trim() }

        // Match user-defined game versions with valid CurseForge version IDs
        val resolvedVersionIds = userVersions.mapNotNull { name ->
            validVersions.firstOrNull { it.name.equals(name, ignoreCase = true) }?.id
        }

        if (resolvedVersionIds.isEmpty()) {
            project.logger.error("❌ Failed to fetch valid game versions from CurseForge. Aborting.")
            return
        }
        project.logger.lifecycle("✅ Retrieved ${resolvedVersionIds.size} valid game version(s).")

        // Build metadata JSON that will be sent with the upload
        project.logger.lifecycle("⏳ Preparing metadata...")

        // Prepare dependency relations (optional)
        val relations = curseforge.dependencies.asMap.values.map {
            ProjectsMetadata(
                projectID = it.projectId,
                relationType = it.relationType
            )
        }

        // Build final metadata structure
        val metadata = CurseforgeMetadata(
            changelog = extension.changelog,
            changelogType = curseforge.changelogType,
            displayName = extension.displayName,
            gameVersions = resolvedVersionIds,
            releaseType = extension.releaseType,
            isMarkedForManualRelease = curseforge.manualRelease,
            relations = Projects(relations)
        )

        val jsonMetadata = Json.encodeToString(metadata)
        project.logger.lifecycle("📦 Metadata prepared: $jsonMetadata")

        // Begin uploading file + metadata
        project.logger.lifecycle("⏳ Uploading to CurseForge...")
        publishingToCurseforge(jsonMetadata, files, projectId!!, token)
    }

    /**
     * Fetches valid Minecraft game versions from the CurseForge API.
     */
    private fun fetchValidGameVersions(token: String): List<GameVersionTag> {
        return try {
            val request = Request.Builder()
                .url(GAME_VERSIONS_URL)
                .addHeader("User-Agent", PublisherConfig.USER_AGENT)
                .addHeader("X-Api-Token", token)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return emptyList()

                val body = response.body.string()
                if (body.isBlank()) return emptyList()

                Json.decodeFromString(body)
            }
        } catch (e: Exception) {
            project.logger.error("❌ Failed to fetch CurseForge game versions: ${e.message}")
            emptyList()
        }
    }

    /**
     * Handles multipart upload to CurseForge (metadata + single mod file).
     */
    private fun publishingToCurseforge(
        jsonMetadata: String,
        files: Set<File>,
        projectId: String,
        token: String
    ) {
        // Build multipart body: metadata + file upload
        val multipartBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)

        // Add metadata JSON part
        multipartBuilder.addFormDataPart(
            "metadata",
            null,
            jsonMetadata.toRequestBody(PublisherConfig.MEDIA_TYPE_JSON.toMediaType())
        )

        // CurseForge accepts only a single file per upload, so we take the first
        val file = files.first()
        project.logger.lifecycle("📁 Attaching file: ${file.name}")

        multipartBuilder.addFormDataPart(
            "file",
            file.name,
            file.asRequestBody(PublisherConfig.MEDIA_TYPE_JAR.toMediaType())
        )

        // Build HTTP request
        val request = Request.Builder()
            .url(String.format(UPLOAD_URL, projectId))
            .addHeader("User-Agent", PublisherConfig.USER_AGENT)
            .addHeader("X-Api-Token", token)
            .post(multipartBuilder.build())
            .build()

        // Execute upload request
        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val body = response.body.string()
                    project.logger.error("❌ Upload failed: ${response.code} ${response.message}")
                    if (body.isNotBlank()) project.logger.error("Response body: $body")
                } else {
                    project.logger.lifecycle("✅ Successfully uploaded to CurseForge!")
                    val responseBody = response.body.string()
                    if (responseBody.isNotBlank()) project.logger.lifecycle("📨 Response: $responseBody")
                }
            }
        } catch (e: Exception) {
            // Handle network or I/O failures
            project.logger.error("❌ Upload to CurseForge failed due to network or I/O error.")
            project.logger.error("   ➜ ${e::class.simpleName}: ${e.message}")
        }
    }
}
