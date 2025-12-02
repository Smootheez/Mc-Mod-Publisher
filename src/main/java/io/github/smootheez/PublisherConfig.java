package io.github.smootheez;

import lombok.*;
import org.gradle.api.*;
import org.gradle.api.model.*;

/**
 * Base configuration class for all publisher integrations (e.g., Modrinth, CurseForge).
 * <p>
 * Provides common fields such as authentication token, project identifier,
 * and a container for declaring dependency relationships.
 * <p>
 * Subclasses must supply concrete dependency creation logic for their respective
 * publishing platforms.
 *
 * @param <T> the dependency model type used by the platform (e.g., CurseforgeDependency)
 * @param <S> the identifier type for declaring a dependency (e.g., String slug or project ID)
 */
@Getter
@Setter
public abstract class PublisherConfig<T, S> {

    /**
     * The authentication token used when publishing.
     * This value is required for all publishing operations.
     */
    private String token;

    /**
     * The platform-specific project identifier.
     * This is required to associate uploaded files with the correct project.
     */
    private String projectId;

    /**
     * Container holding all declared dependency relationships for this publisher,
     * created using Gradle's {@link NamedDomainObjectContainer}.
     */
    protected final NamedDomainObjectContainer<T> dependencies;

    /**
     * Creates a new publisher configuration and initializes the dependency container.
     *
     * @param objects        the Gradle object factory
     * @param dependencyType the class representing the dependency model type
     */
    protected PublisherConfig(ObjectFactory objects, Class<T> dependencyType) {
        this.dependencies = objects.domainObjectContainer(dependencyType);
    }

    /**
     * Provides access to the dependency container for modification.
     *
     * @param action action that configures the dependency container
     */
    public abstract void dependencies(Action<NamedDomainObjectContainer<T>> action);

    /**
     * Declares a required dependency.
     *
     * @param id the identifier for the dependency (slug, project ID, etc.)
     * @return the configured dependency instance
     */
    public abstract T required(S id);

    /**
     * Declares an optional dependency.
     *
     * @param id the identifier for the dependency
     * @return the configured dependency instance
     */
    public abstract T optional(S id);

    /**
     * Declares an incompatible dependency.
     *
     * @param id the identifier for the dependency
     * @return the configured dependency instance
     */
    public abstract T incompatible(S id);

    /**
     * Declares an embedded dependency (e.g., libraries bundled with the mod).
     *
     * @param id the identifier for the dependency
     * @return the configured dependency instance
     */
    public abstract T embedded(S id);
}

