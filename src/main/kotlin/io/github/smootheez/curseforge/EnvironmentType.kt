package io.github.smootheez.curseforge

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class EnvironmentType {
    @SerialName("client")
    CLIENT,

    @SerialName("server")
    SERVER
}