package io.github.smootheez.modrinth;

import com.google.gson.annotations.*;
import lombok.*;

import javax.inject.*;

/**
 * Represents a single dependency entry for a Modrinth project upload.
 * <p>
 * Each dependency specifies another Modrinth project that this mod relies on,
 * optionally, requires, conflicts with, or embeds.
 * Instances are created and managed through {@link ModrinthConfig}.
 */
@Getter
@Setter
public class ModrinthDependency {

    /**
     * The Modrinth project ID of the dependency.
     * Serialized as {@code "project_id"} when communicating with the API.
     */
    @SerializedName("project_id")
    private String projectId;

    /**
     * The type of dependency relationship (required, optional, incompatible, embedded).
     * Serialized as {@code "dependency_type"}.
     */
    @SerializedName("dependency_type")
    private DependencyType dependencyType;

    /**
     * Internal Gradle name used when registering this dependency in
     * a {@code NamedDomainObjectContainer}.
     * Not sent to the Modrinth API.
     */
    private final String name;

    /**
     * Constructs a Modrinth dependency wrapper with the given internal name.
     *
     * @param name the name used to register this dependency inside Gradle's container
     */
    @Inject
    public ModrinthDependency(String name) {
        this.name = name;
    }
}

