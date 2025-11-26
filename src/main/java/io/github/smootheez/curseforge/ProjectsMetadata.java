package io.github.smootheez.curseforge;

import com.google.gson.annotations.*;
import lombok.*;

// Equivalent to ProjectsMetadata
@Builder
public record ProjectsMetadata(String slug,
                               @SerializedName("type") RelationType relationType) {
}
