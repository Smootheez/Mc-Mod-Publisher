package io.github.smootheez.modrinth;

import com.google.gson.annotations.*;
import lombok.*;

@Builder
public record DependencyMetadata(@SerializedName("project_id") String projectId,
                                 @SerializedName("dependency_type") DependencyType dependencyType) {
}
