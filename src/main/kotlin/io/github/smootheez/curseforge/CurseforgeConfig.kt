package io.github.smootheez.curseforge

import io.github.smootheez.McModPublisherDsl
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

@McModPublisherDsl
open class CurseforgeConfig @Inject constructor(objects: ObjectFactory) {

    var token: String? = null
    var projectId: String? = null
    var changelogType: ChangelogType = ChangelogType.MARKDOWN // for consistency with modrinth, set the default changelog type to markdown
    var environmentType: List<EnvironmentType> = mutableListOf(EnvironmentType.CLIENT, EnvironmentType.SERVER)
    var manualRelease: Boolean = false

    val dependencies: NamedDomainObjectContainer<CurseForgeDependency> =
        objects.domainObjectContainer(CurseForgeDependency::class.java)

    fun dependencies(action: NamedDomainObjectContainer<CurseForgeDependency>.() -> Unit) {
        dependencies.action()
    }

    // --- Define dependencies by project ID ---
    fun required(id: Int) =
        dependencies.create("curseforge-$id") {
            this.projectId = id
            relationType = RelationType.REQUIRED_DEPENDENCY
        }

    fun optional(id: Int) =
        dependencies.create("curseforge-$id") {
            this.projectId = id
            relationType = RelationType.OPTIONAL_DEPENDENCY
        }

    fun embedded(id: Int) =
        dependencies.create("curseforge-$id") {
            this.projectId = id
            relationType = RelationType.EMBEDDED_LIBRARY
        }

    fun incompatible(id: Int) =
        dependencies.create("curseforge-$id") {
            this.projectId = id
            relationType = RelationType.INCOMPATIBLE
        }

    fun tool(id: Int) =
        dependencies.create("curseforge-$id") {
            this.projectId = id
            relationType = RelationType.TOOL
        }
}
