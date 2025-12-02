package io.github.smootheez.curseforge;

import lombok.*;

import java.util.List;

/**
 * Represents the metadata payload sent when uploading a file to CurseForge.
 *
 * @param changelog                  the changelog text for this upload
 * @param changelogType              the format used to interpret the changelog (text, markdown, html)
 * @param displayName                the display name of the uploaded file
 * @param gameVersions               numeric CurseForge game version identifiers
 * @param releaseType                the release channel (release, beta, alpha)
 * @param isMarkedForManualRelease   whether the upload requires manual approval
 * @param relations                  dependency relationships declared for this upload
 */
@Builder
public record CurseforgeMetadata(

        String changelog,

        ChangelogType changelogType,

        String displayName,

        List<Integer> gameVersions,

        String releaseType,

        boolean isMarkedForManualRelease,

        Projects relations
) { }


