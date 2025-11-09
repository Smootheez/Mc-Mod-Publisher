package io.github.smootheez.modrinth

import javax.inject.Inject

open class ModrinthDependency @Inject constructor(val name: String) {
    var projectId: String? = null
    var dependencyType: DependencyType = DependencyType.REQUIRED
}
