package io.github.smootheez.curseforge;

import com.google.gson.annotations.*;

/**
 * Defines the possible relationship types between a project and another
 * referenced project or resource. These values are typically used when
 * declaring dependencies or associations in metadata structures.
 * <p>
 * Each enum constant is annotated with {@link com.google.gson.annotations.SerializedName}
 * to ensure correct JSON mapping when serializing or deserializing metadata.
 */
public enum RelationType {

    /**
     * Indicates that the referenced project is bundled directly inside this project
     * as an embedded library. It is not downloaded separately.
     */
    @SerializedName("embeddedLibrary")
    EMBEDDED_LIBRARY,

    /**
     * Indicates that the referenced project conflicts with this one. Both cannot
     * be installed or used together.
     */
    @SerializedName("incompatible")
    INCOMPATIBLE,

    /**
     * Indicates that the referenced project is an optional dependency. The project
     * can function without it, but may provide additional features when present.
     */
    @SerializedName("optionalDependency")
    OPTIONAL_DEPENDENCY,

    /**
     * Indicates that the referenced project is a required dependency. This project
     * cannot function without it.
     */
    @SerializedName("requiredDependency")
    REQUIRED_DEPENDENCY,

    /**
     * Indicates that the referenced project is a tool or utility associated with this
     * project, typically not a runtime dependency.
     */
    @SerializedName("tool")
    TOOL
}
