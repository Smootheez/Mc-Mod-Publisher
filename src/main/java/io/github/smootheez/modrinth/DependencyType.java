package io.github.smootheez.modrinth;

import com.google.gson.annotations.*;

public enum DependencyType {
    @SerializedName("required")
    REQUIRED,

    @SerializedName("optional")
    OPTIONAL,

    @SerializedName("incompatible")
    INCOMPATIBLE,

    @SerializedName("embedded")
    EMBEDDED
}
