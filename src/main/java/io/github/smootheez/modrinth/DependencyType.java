package io.github.smootheez.modrinth;

import com.google.gson.annotations.*;

/**
 * Represents the type of dependency relationship used when publishing
 * metadata to mod distribution platforms such as Modrinth or CurseForge.
 * <p>
 * Each enum value corresponds to a specific semantic meaning describing
 * how the current project interacts with another referenced project.
 * The values are serialized using Gson via the {@link SerializedName}
 * annotations to match the expected JSON schema.
 */
public enum DependencyType {

    /**
     * Indicates that the dependency is mandatory.
     * The mod cannot load or function correctly without this project.
     * Serialized as {@code "required"}.
     */
    @SerializedName("required")
    REQUIRED,

    /**
     * Indicates that the dependency is optional.
     * The mod can run without it, but may enable extra features if present.
     * Serialized as {@code "optional"}.
     */
    @SerializedName("optional")
    OPTIONAL,

    /**
     * Marks a dependency as incompatible.
     * If the referenced project is installed, the mod should not load.
     * Serialized as {@code "incompatible"}.
     */
    @SerializedName("incompatible")
    INCOMPATIBLE,

    /**
     * Indicates that the dependency is bundled or embedded directly inside
     * the mod’s own files, meaning users do not need to install it separately.
     * Serialized as {@code "embedded"}.
     */
    @SerializedName("embedded")
    EMBEDDED
}
