package io.github.smootheez.curseforge

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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