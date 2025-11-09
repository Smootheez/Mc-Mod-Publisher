package io.github.smootheez.curseforge

import javax.inject.Inject

open class CurseForgeDependency @Inject constructor(val name: String) {
    var projectId: Int? = null
    var relationType: RelationType = RelationType.REQUIRED_DEPENDENCY
}
