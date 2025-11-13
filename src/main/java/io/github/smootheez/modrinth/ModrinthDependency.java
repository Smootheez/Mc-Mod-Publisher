package io.github.smootheez.modrinth;

import lombok.*;

import javax.inject.*;

@Getter
@Setter
public class ModrinthDependency {
    private String projectId;
    private DependencyType dependencyType;

    private final String name;

    @Inject
    public ModrinthDependency(String name) {
        this.name = name;
    }
}
