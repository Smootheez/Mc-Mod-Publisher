package io.github.smootheez.modrinth;

import com.google.gson.annotations.*;
import lombok.*;

@Builder
public record GameVersionTag(String version, @SerializedName("version_type") String versionType, String date,
                             boolean major) {
}
