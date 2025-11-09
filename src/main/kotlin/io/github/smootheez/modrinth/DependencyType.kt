package io.github.smootheez.modrinth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class DependencyType {
    @SerialName("required")
    REQUIRED,

    @SerialName("optional")
    OPTIONAL,

    @SerialName("incompatible")
    INCOMPATIBLE,

    @SerialName("embedded")
    EMBEDDED
}