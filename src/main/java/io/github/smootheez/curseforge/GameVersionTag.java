package io.github.smootheez.curseforge;

import com.google.gson.annotations.*;
import lombok.*;

/**
 * Represents a game version entry returned by the CurseForge API.
 * <p>
 * Each tag describes a version identifier, its classification type,
 * and human-readable metadata. These tags are used to match Minecraft
 * versions, mod loaders, and environment indicators (client/server)
 * during publication.
 *
 * @param id                unique numeric identifier for the version tag
 * @param gameVersionTypeId internal CurseForge type ID indicating whether the tag
 *                          refers to a Minecraft version, a loader, or an environment
 * @param name              display name of the version tag (e.g., "1.20.1", "Fabric", "Client")
 * @param slug              URL-friendly identifier associated with the version tag
 */
@Builder
public record GameVersionTag(
        int id,
        @SerializedName("gameVersionTypeID") int gameVersionTypeId,
        String name,
        String slug
) {
}

