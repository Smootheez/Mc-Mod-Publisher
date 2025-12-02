package io.github.smootheez.modrinth;

import com.google.gson.annotations.*;
import io.github.smootheez.*;
import lombok.*;

import java.util.*;

/**
 * Represents the complete metadata payload required when uploading a version to Modrinth.
 *
 * @param projectId    the unique project ID on Modrinth
 * @param name         the display name of the version
 * @param versionNumber the semantic or custom version identifier
 * @param changelog    the changelog text associated with this version
 * @param gameVersions list of supported game versions (e.g. "1.20.1")
 * @param loaders      the supported mod loaders (Fabric, Forge, Quilt, etc.)
 * @param releaseChannel the release channel (release, beta, alpha)
 * @param dependencies  dependency metadata for this version
 * @param featured      whether the version should be featured
 * @param status        the Modrinth version status (listed, draft, archived, etc.)
 * @param fileParts     file identifiers used during multipart upload
 */
@Builder
public record ModrinthMetadata(

        @SerializedName("project_id")
        String projectId,

        @SerializedName("name")
        String name,

        @SerializedName("version_number")
        String versionNumber,

        @SerializedName("changelog")
        String changelog,

        @SerializedName("game_versions")
        List<String> gameVersions,

        @SerializedName("loaders")
        List<LoaderType> loaders,

        @SerializedName("release_channel")
        String releaseChannel,

        @SerializedName("dependencies")
        List<DependencyMetadata> dependencies,

        @SerializedName("featured")
        boolean featured,

        @SerializedName("status")
        String status,

        @SerializedName("file_parts")
        List<String> fileParts
) { }

