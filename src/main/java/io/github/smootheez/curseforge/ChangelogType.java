package io.github.smootheez.curseforge;

import com.google.gson.annotations.*;

public enum ChangelogType {
    @SerializedName("text")
    TEXT,

    @SerializedName("markdown")
    MARKDOWN,

    @SerializedName("html")
    HTML
}
