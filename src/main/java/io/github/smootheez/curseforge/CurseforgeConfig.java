package io.github.smootheez.curseforge;

import io.github.smootheez.*;
import lombok.*;
import org.gradle.api.*;
import org.gradle.api.model.*;

import javax.inject.*;
import java.util.*;

@Getter
@Setter
public class CurseforgeConfig extends PublisherConfig<CurseforgeDependency, String> {
    private ChangelogType changelogType = ChangelogType.MARKDOWN;
    private List<EnvironmentType> environmentType = List.of(EnvironmentType.CLIENT, EnvironmentType.SERVER);
    private boolean manualRelease = false;

    private static final String CURSEFORGE =  "curseforge-";

    @Inject
    public CurseforgeConfig(ObjectFactory objects) {
        super(objects, CurseforgeDependency.class);
    }

    @Override
    public void dependencies(Action<NamedDomainObjectContainer<CurseforgeDependency>> action) {
        action.execute(dependencies);
    }

    @Override
    public CurseforgeDependency required(String slug) {
        return createDependency(slug, RelationType.REQUIRED_DEPENDENCY);
    }

    @Override
    public CurseforgeDependency optional(String slug) {
        return createDependency(slug, RelationType.OPTIONAL_DEPENDENCY);
    }

    @Override
    public CurseforgeDependency incompatible(String slug) {
        return createDependency(slug, RelationType.INCOMPATIBLE);
    }

    @Override
    public CurseforgeDependency embedded(String slug) {
        return createDependency(slug, RelationType.EMBEDDED_LIBRARY);
    }

    public CurseforgeDependency tool(String slug) {
        return createDependency(slug, RelationType.TOOL);
    }

    private CurseforgeDependency createDependency(String slug, RelationType type) {
        String name = CURSEFORGE + slug;
        CurseforgeDependency dependency = dependencies.create(name);
        dependency.setSlug(slug);
        dependency.setRelationType(type);
        return dependency;
    }
}
