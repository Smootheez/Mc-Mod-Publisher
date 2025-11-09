package io.github.smootheez.curseforge

import kotlinx.serialization.Serializable

@Serializable
data class CurseforgeMetadata(
//    val projectId: String, not include the project id because project id is requested by parameter
    val changelog: String,
    val changelogType: ChangelogType,
    val displayName: String? = null,
//    val parentFileID: String? = null, don't need parentID
    val gameVersions: List<Int>,
    val releaseType: String, //
    val isMarkedForManualRelease: Boolean,
    val relations: Projects
)

@Serializable
data class Projects(
    val projects: List<ProjectsMetadata> = emptyList()
)

@Serializable
data class ProjectsMetadata(
    val projectID: Int?,
    val relationType: RelationType
)

