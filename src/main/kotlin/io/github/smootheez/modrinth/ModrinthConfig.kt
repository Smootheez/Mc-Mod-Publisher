package io.github.smootheez.modrinth

import io.github.smootheez.McModPublisherDsl
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

@McModPublisherDsl
open class ModrinthConfig @Inject constructor(objects: ObjectFactory) {

    var token: String? = null
    var projectId: String? = null
    var featured: Boolean = true
    var status: String = "listed" // a new version, Modrinth defaults status to "listed". nullable with defaults to simplify upload calls

    val dependencies: NamedDomainObjectContainer<ModrinthDependency> =
        objects.domainObjectContainer(ModrinthDependency::class.java)

    fun dependencies(action: NamedDomainObjectContainer<ModrinthDependency>.() -> Unit) {
        dependencies.action()
    }

    fun required(id: String) =
        dependencies.create("modrinth-$id") {
            this.projectId = id
            dependencyType = DependencyType.REQUIRED
        }

    fun optional(id: String) =
        dependencies.create("modrinth-$id") {
            this.projectId = id
            dependencyType = DependencyType.OPTIONAL
        }

    fun incompatible(id: String) =
        dependencies.create("modrinth-$id") {
            this.projectId = id
            dependencyType = DependencyType.INCOMPATIBLE
        }

    fun embedded(id: String) =
        dependencies.create("modrinth-$id") {
            this.projectId = id
            dependencyType = DependencyType.EMBEDDED
        }
}
