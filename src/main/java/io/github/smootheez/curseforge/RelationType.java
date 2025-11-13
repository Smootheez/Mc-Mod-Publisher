package io.github.smootheez.curseforge;

import com.google.gson.annotations.*;

public enum RelationType {
    @SerializedName("embeddedLibrary")
    EMBEDDED_LIBRARY,

    @SerializedName("incompatible")
    INCOMPATIBLE,

    @SerializedName("optionalDependency")
    OPTIONAL_DEPENDENCY,

    @SerializedName("requiredDependency")
    REQUIRED_DEPENDENCY,

    @SerializedName("tool")
    TOOL
}
