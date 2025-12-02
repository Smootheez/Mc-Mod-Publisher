package io.github.smootheez.curseforge;

import io.github.smootheez.*;
import lombok.*;
import org.gradle.api.*;
import org.gradle.api.model.*;

import javax.inject.*;
import java.util.*;

/**
 * Configuration model for publishing projects to CurseForge.
 * <p>
 * Extends {@link PublisherConfig} to provide CurseForge-specific options such as
 * changelog format, environment targeting, and dependency declarations.
 */
@Getter
@Setter
public class CurseforgeConfig extends PublisherConfig<CurseforgeDependency, String> {

    /**
     * The format used when uploading the changelog to CurseForge.
     * Defaults to {@link ChangelogType#MARKDOWN}.
     */
    private ChangelogType changelogType = ChangelogType.MARKDOWN;

    /**
     * The set of supported environments for this release (e.g., client, server).
     * Defaults to both {@link EnvironmentType#CLIENT} and {@link EnvironmentType#SERVER}.
     */
    private List<EnvironmentType> environmentType =
            List.of(EnvironmentType.CLIENT, EnvironmentType.SERVER);

    /**
     * Whether publishing requires manual release approval on CurseForge.
     * Defaults to {@code false}.
     */
    private boolean manualRelease = false;

    /** Prefix applied to dependency names created for CurseForge. */
    private static final String CURSEFORGE = "curseforge-";

    /**
     * Constructs a new CurseForge configuration block.
     *
     * @param objects the Gradle object factory used to create domain objects
     */
    @Inject
    public CurseforgeConfig(ObjectFactory objects) {
        super(objects, CurseforgeDependency.class);
    }

    /**
     * Provides access to the dependency container for configuration.
     *
     * @param action the configuration action to apply to the dependency container
     */
    @Override
    public void dependencies(Action<NamedDomainObjectContainer<CurseforgeDependency>> action) {
        action.execute(dependencies);
    }

    /**
     * Declares a required CurseForge dependency.
     *
     * @param slug the CurseForge project slug
     * @return the created dependency entry
     */
    @Override
    public CurseforgeDependency required(String slug) {
        return createDependency(slug, RelationType.REQUIRED_DEPENDENCY);
    }

    /**
     * Declares an optional CurseForge dependency.
     *
     * @param slug the CurseForge project slug
     * @return the created dependency entry
     */
    @Override
    public CurseforgeDependency optional(String slug) {
        return createDependency(slug, RelationType.OPTIONAL_DEPENDENCY);
    }

    /**
     * Declares an incompatible CurseForge dependency.
     *
     * @param slug the CurseForge project slug
     * @return the created dependency entry
     */
    @Override
    public CurseforgeDependency incompatible(String slug) {
        return createDependency(slug, RelationType.INCOMPATIBLE);
    }

    /**
     * Declares an embedded library dependency.
     *
     * @param slug the CurseForge project slug
     * @return the created dependency entry
     */
    @Override
    public CurseforgeDependency embedded(String slug) {
        return createDependency(slug, RelationType.EMBEDDED_LIBRARY);
    }

    /**
     * Declares a tool dependency on CurseForge.
     *
     * @param slug the CurseForge project slug
     * @return the created dependency entry
     */
    public CurseforgeDependency tool(String slug) {
        return createDependency(slug, RelationType.TOOL);
    }

    /**
     * Creates and registers a CurseForge dependency with the given relation type.
     *
     * @param slug the CurseForge project slug
     * @param type the dependency relation type
     * @return the created dependency instance
     */
    private CurseforgeDependency createDependency(String slug, RelationType type) {
        String name = CURSEFORGE + slug;
        CurseforgeDependency dependency = dependencies.create(name);
        dependency.setSlug(slug);
        dependency.setRelationType(type);
        return dependency;
    }
}

