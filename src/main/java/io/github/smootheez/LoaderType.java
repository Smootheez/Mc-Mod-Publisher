package io.github.smootheez;

import com.google.gson.annotations.*;

/**
 * Represents the supported mod loader ecosystems used when determining
 * compatibility for publication on platforms such as Modrinth or CurseForge.
 * <p>
 * Each value is annotated with {@link SerializedName} to ensure correct
 * JSON mapping when communicating with external APIs.
 */
public enum LoaderType {

    /**
     * Indicates compatibility with the Fabric mod loader.
     * Serialized as {@code "fabric"}.
     */
    @SerializedName("fabric")
    FABRIC,

    /**
     * Indicates compatibility with the Quilt mod loader.
     * Serialized as {@code "quilt"}.
     */
    @SerializedName("quilt")
    QUILT,

    /**
     * Indicates compatibility with the Forge mod loader.
     * Serialized as {@code "forge"}.
     */
    @SerializedName("forge")
    FORGE,

    /**
     * Indicates compatibility with the NeoForge mod loader.
     * Serialized as {@code "neoforge"}.
     */
    @SerializedName("neoforge")
    NEOFORGE
}

