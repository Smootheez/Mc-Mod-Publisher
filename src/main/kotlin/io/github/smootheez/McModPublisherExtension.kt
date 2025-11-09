package io.github.smootheez

import io.github.smootheez.curseforge.CurseforgeConfig
import io.github.smootheez.modrinth.ModrinthConfig
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

@McModPublisherDsl
open class McModPublisherExtension @Inject constructor(
    project: Project,
    objects: ObjectFactory
) {

    // --- Shared metadata ---
    var displayName: String? = null
    var version: String? = null
    var releaseType: String = "release"  // "release", "beta", "alpha"
    var changelog: String = ""

    /** Optional override for supported game versions and loaders */
    val gameVersions = mutableListOf<String>()
    val loaders = mutableListOf<LoaderType>()

    // --- Artifact(s) to upload ---
    val files: ConfigurableFileCollection = project.files()

    // --- Platform configurations ---
    val curseforge = objects.newInstance(CurseforgeConfig::class.java, objects)
    val modrinth = objects.newInstance(ModrinthConfig::class.java, objects)

    fun curseforge(block: CurseforgeConfig.() -> Unit) = block(curseforge)
    fun modrinth(block: ModrinthConfig.() -> Unit) = block(modrinth)
}
