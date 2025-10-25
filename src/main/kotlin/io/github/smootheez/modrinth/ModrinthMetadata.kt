package io.github.smootheez.modrinth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModrinthMetadata(
    @SerialName("name")
    val name: String,

    @SerialName("version_number")
    val versionNumber: String,

    @SerialName("changelog")
    val changelog: String,

    @SerialName("project_id")
    val projectId: String,

    @SerialName("game_versions")
    val gameVersions: List<String>,

    @SerialName("loaders")
    val loaders: List<String>,

    @SerialName("version_type")
    val versionType: ReleaseType = ReleaseType.RELEASE, // default to RELEASE, which is the most common case

    @SerialName("dependencies")
    val dependencies: List<DependencyMetadata> = emptyList(),

    @SerialName("featured")
    val featured: Boolean = true,

    // a new version, Modrinth defaults status to "listed". nullable with defaults to simplify upload calls
    @SerialName("status")
    val status: Status? = null,

    @SerialName("requested_status")
    val requestedStatus: Status? = null,
)

@Serializable
enum class Status {
    @SerialName("listed") LISTED,
    @SerialName("archived") ARCHIVED,
    @SerialName("draft") DRAFT,
    @SerialName("unlisted") UNLISTED,
    @SerialName("scheduled") SCHEDULED
}

@Serializable
enum class ReleaseType {
    @SerialName("release") RELEASE,
    @SerialName("beta") BETA,
    @SerialName("alpha") ALPHA
}

@Serializable
data class DependencyMetadata(
    @SerialName("version_id")
    val versionId: String? = null,
    @SerialName("project_id")
    val projectId: String? = null,
    @SerialName("file_name")
    val fileName: String? = null,
    @SerialName("dependency_type")
    val dependencyType: DependencyType = DependencyType.REQUIRED
)

@Serializable
enum class DependencyType {
    @SerialName("required") REQUIRED,
    @SerialName("optional") OPTIONAL,
    @SerialName("incompatible") INCOMPATIBLE,
    @SerialName("embedded") EMBEDDED
}
