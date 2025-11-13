package io.github.smootheez.curseforge;

import lombok.*;

import java.util.List;

// Equivalent to CurseforgeMetadata
@Builder
public record CurseforgeMetadata(String changelog,
                                 ChangelogType changelogType,
                                 String displayName,
                                 List<Integer> gameVersions,
                                 String releaseType,
                                 boolean isMarkedForManualRelease,
                                 Projects relations) {
}

