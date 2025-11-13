package io.github.smootheez.curseforge;

import com.google.gson.annotations.*;

public enum EnvironmentType {
    @SerializedName("client")
    CLIENT,

    @SerializedName("server")
    SERVER
}
