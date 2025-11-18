package io.github.smootheez.modrinth;

import com.google.gson.annotations.*;
import lombok.*;

import javax.inject.*;

@Getter
@Setter
public class ModrinthDependency {
    @SerializedName("project_id")
    private String projectId;
    @SerializedName("dependency_type")
    private DependencyType dependencyType;

    private final String name;

    @Inject
    public ModrinthDependency(String name) {
        this.name = name;
    }
}
