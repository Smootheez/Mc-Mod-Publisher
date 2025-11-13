package io.github.smootheez.modrinth;

import io.github.smootheez.*;
import lombok.*;
import org.gradle.api.*;
import org.gradle.api.model.*;

import javax.inject.*;

@Getter
@Setter
public class ModrinthConfig extends PublisherConfig<ModrinthDependency, String> {
    private boolean featured = true;
    private String status = "listed";

    private static final String MODRINTH = "modrinth-";

    @Inject
    public ModrinthConfig(ObjectFactory objects) {
        super(objects, ModrinthDependency.class);
    }

    @Override
    public void dependencies(Action<NamedDomainObjectContainer<ModrinthDependency>> action) {
        action.execute(dependencies);
    }

    @Override
    public ModrinthDependency required(String id) {
        return createDependency(id, DependencyType.REQUIRED);
    }

    @Override
    public ModrinthDependency optional(String id) {
        return createDependency(id, DependencyType.OPTIONAL);
    }

    @Override
    public ModrinthDependency incompatible(String id) {
        return createDependency(id, DependencyType.INCOMPATIBLE);
    }

    @Override
    public ModrinthDependency embedded(String id) {
        return createDependency(id, DependencyType.EMBEDDED);
    }

    private ModrinthDependency createDependency(String projectId, DependencyType type) {
        String name = MODRINTH + projectId;
        ModrinthDependency dependency = dependencies.create(name);
        dependency.setProjectId(projectId);
        dependency.setDependencyType(type);
        return dependency;
    }
}
