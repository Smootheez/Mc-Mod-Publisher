package io.github.smootheez.curseforge;

import com.google.gson.annotations.*;
import lombok.*;

@Builder
public record GameVersionTag(int id, @SerializedName("gameVersionTypeID") int gameVersionTypeId,
                             String name, String slug) {
}
