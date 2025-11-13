package io.github.smootheez.modrinth;

import com.google.gson.annotations.*;
import io.github.smootheez.*;
import lombok.*;

import java.util.*;

/**
 * @param name nullable
 */
@Builder
public record ModrinthMetadata(@SerializedName("project_id") String projectId, @SerializedName("name") String name,
                               @SerializedName("version_number") String versionNumber,
                               @SerializedName("changelog") String changelog,
                               @SerializedName("game_versions") List<String> gameVersions,
                               @SerializedName("loaders") List<LoaderType> loaders,
                               @SerializedName("release_channel") String releaseChannel,
                               @SerializedName("dependencies") List<DependencyMetadata> dependencies,
                               @SerializedName("featured") boolean featured, @SerializedName("status") String status,
                               @SerializedName("file_parts") List<String> fileParts) {
}
