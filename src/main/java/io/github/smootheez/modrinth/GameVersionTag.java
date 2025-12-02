package io.github.smootheez.modrinth;

import com.google.gson.annotations.*;
import lombok.*;

/**
 * Represents a game version entry retrieved from an external API such as Modrinth.
 * <p>
 * Each version includes metadata describing its release type, publication date,
 * and whether it is considered a major release. This information is typically used
 * to determine compatibility or filter target versions during publishing.
 *
 * @param version      the human-readable version string (e.g., "1.20.1")
 * @param versionType  classification of the version (e.g., "release", "snapshot", "beta");
 *                     mapped from the JSON field {@code version_type}
 * @param date         the release date of the version in ISO-8601 format
 * @param major        whether this version is marked as a major update
 */
@Builder
public record GameVersionTag(
        String version,
        @SerializedName("version_type") String versionType,
        String date,
        boolean major
) {
}

