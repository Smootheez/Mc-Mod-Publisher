package io.github.smootheez.curseforge

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ChangelogType {
    @SerialName("text")
    TEXT,

    @SerialName("markdown")
    MARKDOWN,

    @SerialName("html")
    HTML
}