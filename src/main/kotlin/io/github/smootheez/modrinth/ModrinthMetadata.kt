package io.github.smootheez.modrinth

import io.github.smootheez.LoaderType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModrinthMetadata(
    @SerialName("project_id")
    val projectId: String,

    @SerialName("name")
    val name: String? = null,

    @SerialName("version_number")
    val versionNumber: String,

    @SerialName("changelog")
    val changelog: String,

    @SerialName("game_versions")
    val gameVersions: List<String>,

    @SerialName("loaders")
    val loaders: List<LoaderType>,

    @SerialName("release_channel")
    val releaseChannel: String, // default to RELEASE, which is the most common case

    @SerialName("dependencies")
    val dependencies: List<DependencyMetadata> = emptyList(),

    @SerialName("featured")
    val featured: Boolean,

    // a new version, Modrinth defaults status to "listed". nullable with defaults to simplify upload calls
    @SerialName("status")
    val status: String = "listed",

    @SerialName("file_parts")
    val fileParts: List<String>
)

@Serializable
data class DependencyMetadata(
    @SerialName("project_id")
    val projectId: String?,

    @SerialName("dependency_type")
    val dependencyType: DependencyType
)

