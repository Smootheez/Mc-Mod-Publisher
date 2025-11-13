package io.github.smootheez;

import com.google.gson.annotations.*;

public enum LoaderType {
    @SerializedName("fabric")
    FABRIC,

    @SerializedName("quilt")
    QUILT,

    @SerializedName("forge")
    FORGE,

    @SerializedName("neoforge")
    NEOFORGE
}
