package io.github.smootheez.curseforge;

import com.google.gson.annotations.*;

/**
 * Defines the supported changelog content formats used by curseforge.
 */
public enum ChangelogType {

    /** Changelog provided as plain text. */
    @SerializedName("text")
    TEXT,

    /** Changelog written using Markdown syntax. */
    @SerializedName("markdown")
    MARKDOWN,

    /** Changelog provided as HTML content. */
    @SerializedName("html")
    HTML
}

