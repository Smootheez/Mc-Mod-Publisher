package io.github.smootheez.modrinth;

import com.google.gson.annotations.*;
import lombok.*;

/**
 * Represents a single dependency entry used when publishing metadata
 * to external mod distribution platforms such as CurseForge or Modrinth.
 * <p>
 * This record models the relationship between the current project and another
 * referenced project, typically used to express requirements such as:
 * <ul>
 *     <li>required dependencies</li>
 *     <li>optional dependencies</li>
 *     <li>embedded libraries</li>
 *     <li>tooling or compatibility addons</li>
 * </ul>
 *
 * <p>The values here are serialized to JSON using Gson and included as part
 * of the upload request payload.
 *
 * @param projectId       The unique identifier or slug of the dependent project,
 *                        as recognized by the remote platform. Annotated with
 *                        {@code project_id} for correct JSON mapping.
 * @param dependencyType  The relationship type between this project and the
 *                        dependency, defining whether it is required,
 *                        optional, incompatible, or has other semantics.
 *                        Annotated with {@code dependency_type} for JSON mapping.
 */
@Builder
public record DependencyMetadata(
        @SerializedName("project_id") String projectId,
        @SerializedName("dependency_type") DependencyType dependencyType
) { }

