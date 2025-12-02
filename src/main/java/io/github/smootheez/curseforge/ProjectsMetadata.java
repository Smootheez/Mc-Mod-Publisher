package io.github.smootheez.curseforge;

import com.google.gson.annotations.*;
import lombok.*;

/**
 * Represents a single relationship entry linking this upload to another project.
 *
 * @param slug         the project slug of the related project
 * @param relationType the type of dependency or relation
 */
@Builder
public record ProjectsMetadata(
        String slug,
        @SerializedName("type") RelationType relationType
) { }
