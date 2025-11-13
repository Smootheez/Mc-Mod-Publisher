package io.github.smootheez;

import io.github.smootheez.curseforge.*;
import io.github.smootheez.modrinth.*;
import lombok.*;
import org.gradle.api.*;
import org.gradle.api.file.*;

import javax.annotation.*;
import javax.inject.*;
import java.util.*;

@Getter
@Setter
public class McModPublisherExtension {
    @Nullable
    private String displayName;
    private String version; // Not null
    private String releaseType = "release"; // make the default upload 'release'
    private String changelog = ""; // empty string changelog instead of null

    private final List<String> gameVersions = new ArrayList<>();
    private final List<LoaderType> loaders = new ArrayList<>();

    private final ConfigurableFileCollection files;

    private final CurseforgeConfig curseforge;
    private final ModrinthConfig modrinth;

    @Inject
    public McModPublisherExtension(ConfigurableFileCollection files, CurseforgeConfig curseforge, ModrinthConfig modrinth) {
        this.files = files;
        this.curseforge = curseforge;
        this.modrinth = modrinth;
    }

    public void modrinth(Action<ModrinthConfig> action) {
        action.execute(modrinth);
    }

    public void curseforge(Action<CurseforgeConfig> action) {
        action.execute(curseforge);
    }
}
