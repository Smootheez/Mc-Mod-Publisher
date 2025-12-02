package io.github.smootheez.modrinth;

import io.github.smootheez.*;
import lombok.*;
import org.gradle.api.*;
import org.gradle.api.model.*;

import javax.inject.*;

/**
 * Configuration block for publishing mods to Modrinth.
 * <p>
 * This class extends {@link PublisherConfig} to provide Modrinth-specific
 * dependency handling and upload options such as featured status and version
 * listing state.
 */
@Getter
@Setter
public class ModrinthConfig extends PublisherConfig<ModrinthDependency, String> {

    /**
     * Determines whether the uploaded version should be marked as featured
     * on Modrinth. Defaults to {@code true}.
     */
    private boolean featured = true;

    /**
     * Indicates the visibility status of the uploaded version
     * (e.g., {@code "listed"}, {@code "unlisted"}, {@code "draft"}).
     * Defaults to {@code "listed"}.
     */
    private String status = "listed";

    /** Prefix used when generating names for Modrinth dependency entries. */
    private static final String MODRINTH = "modrinth-";

    /**
     * Creates a new Modrinth configuration instance and initializes the
     * dependency container.
     *
     * @param objects Gradle object factory used to construct dependency instances
     */
    @Inject
    public ModrinthConfig(ObjectFactory objects) {
        super(objects, ModrinthDependency.class);
    }

    /**
     * Applies the provided configuration action to the Modrinth dependency container.
     *
     * @param action an action that configures the dependency container
     */
    @Override
    public void dependencies(Action<NamedDomainObjectContainer<ModrinthDependency>> action) {
        action.execute(dependencies);
    }

    /**
     * Creates a required dependency entry for the given Modrinth project ID.
     *
     * @param id project ID on Modrinth
     * @return the created dependency
     */
    @Override
    public ModrinthDependency required(String id) {
        return createDependency(id, DependencyType.REQUIRED);
    }

    /**
     * Creates an optional dependency entry for the given Modrinth project ID.
     *
     * @param id project ID on Modrinth
     * @return the created dependency
     */
    @Override
    public ModrinthDependency optional(String id) {
        return createDependency(id, DependencyType.OPTIONAL);
    }

    /**
     * Creates an incompatible dependency entry for the given Modrinth project ID.
     *
     * @param id project ID on Modrinth
     * @return the created dependency
     */
    @Override
    public ModrinthDependency incompatible(String id) {
        return createDependency(id, DependencyType.INCOMPATIBLE);
    }

    /**
     * Creates an embedded dependency entry for the given Modrinth project ID.
     *
     * @param id project ID on Modrinth
     * @return the created dependency
     */
    @Override
    public ModrinthDependency embedded(String id) {
        return createDependency(id, DependencyType.EMBEDDED);
    }

    /**
     * Internal helper that constructs a Modrinth dependency with the given type.
     *
     * @param projectId Modrinth project ID
     * @param type      type of dependency relationship
     * @return the configured dependency object
     */
    private ModrinthDependency createDependency(String projectId, DependencyType type) {
        String name = MODRINTH + projectId;
        ModrinthDependency dependency = dependencies.create(name);
        dependency.setProjectId(projectId);
        dependency.setDependencyType(type);
        return dependency;
    }
}

