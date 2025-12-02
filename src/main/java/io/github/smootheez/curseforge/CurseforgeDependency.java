package io.github.smootheez.curseforge;

import lombok.*;

import javax.inject.*;

/**
 * Represents a CurseForge dependency declaration within a publisher configuration.
 * <p>
 * Each dependency is identified by a project slug and associated with a specific
 * {@link RelationType}, which determines how the dependency is treated by CurseForge
 * (required, optional, incompatible, etc.).
 */
@Getter
@Setter
public class CurseforgeDependency {

    /**
     * The CurseForge project slug that identifies the dependency target.
     */
    private String slug;

    /**
     * The relationship type that describes how this dependency is used by the project.
     * For example: required, optional, incompatible, embedded, or tool.
     */
    private RelationType relationType;

    /**
     * The unique name assigned by Gradle when the dependency is created
     * in a {@link org.gradle.api.NamedDomainObjectContainer}.
     */
    private final String name;

    /**
     * Constructs a dependency instance using its container-managed name.
     *
     * @param name the Gradle domain object name
     */
    @Inject
    public CurseforgeDependency(String name) {
        this.name = name;
    }
}

