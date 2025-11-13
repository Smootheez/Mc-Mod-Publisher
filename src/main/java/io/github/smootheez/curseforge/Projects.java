package io.github.smootheez.curseforge;

import java.util.*;

// Equivalent to Projects
public record Projects(
        List<ProjectsMetadata> projects
) {
    // default to empty list if needed
    public Projects() {
        this(List.of());
    }
}
