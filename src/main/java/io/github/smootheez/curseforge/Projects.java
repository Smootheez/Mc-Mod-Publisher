package io.github.smootheez.curseforge;

import java.util.*;

/**
 * Container for a list of project relation metadata entries.
 *
 * @param projects the list of project metadata entries
 */
public record Projects(
        List<ProjectsMetadata> projects
) {

    /**
     * Creates a {@code Projects} instance with an empty list by default.
     */
    public Projects() {
        this(List.of());
    }
}

