package io.github.smootheez.curseforge;

import io.github.smootheez.*;
import lombok.*;
import org.gradle.api.*;
import org.gradle.api.model.*;

import javax.inject.*;
import java.util.*;

@Getter
@Setter
public class CurseforgeConfig extends PublisherConfig<CurseforgeDependency, Integer> {
    private ChangelogType changelogType;
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
    public CurseforgeDependency required(Integer id) {
        return createDependency(id, RelationType.REQUIRED_DEPENDENCY);
    }

    @Override
    public CurseforgeDependency optional(Integer id) {
        return createDependency(id, RelationType.OPTIONAL_DEPENDENCY);
    }

    @Override
    public CurseforgeDependency incompatible(Integer id) {
        return createDependency(id, RelationType.INCOMPATIBLE);
    }

    @Override
    public CurseforgeDependency embedded(Integer id) {
        return createDependency(id, RelationType.EMBEDDED_LIBRARY);
    }

    public CurseforgeDependency tool(Integer id) {
        return createDependency(id, RelationType.TOOL);
    }

    private CurseforgeDependency createDependency(int projectId, RelationType type) {
        String name = CURSEFORGE + projectId;
        CurseforgeDependency dependency = dependencies.create(name);
        dependency.setProjectId(projectId);
        dependency.setRelationType(type);
        return dependency;
    }
}
