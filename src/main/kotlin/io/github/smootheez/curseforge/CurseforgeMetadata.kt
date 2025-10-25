package io.github.smootheez.curseforge

import io.github.smootheez.core.ReleaseType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurseforgeMetadata(
    val projectId: String,
    val changelog: String? = null,
    val changelogType: ChangelogType? = null,
    val displayName: String? = null,
    val parentFileID: String? = null,
    val gameVersions: List<String>,
    val releaseType: ReleaseType,
    val isMarkedForManualRelease: Boolean = true,
    val relations: Projects
)

data class Projects(
    val projects: List<ProjectsMetadata> = emptyList()
)

data class ProjectsMetadata(
    val slug: String? = null,
    val projectID: String? = null,
    val relationType: RelationType
)

@Serializable
enum class RelationType {
    @SerialName("embeddedLibrary")
    EMBEDDED_LIBRARY,

    @SerialName("incompatible")
    INCOMPATIBLE,

    @SerialName("optionalDependency")
    OPTIONAL_DEPENDENCY,

    @SerialName("requiredDependency")
    REQUIRED_DEPENDENCY,

    @SerialName("tool")
    TOOL
}

@Serializable
enum class ChangelogType {
    @SerialName("text")
    TEXT,

    @SerialName("markdown")
    MARKDOWN,

    @SerialName("html")
    HTML
}
