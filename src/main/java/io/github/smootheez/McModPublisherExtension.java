package io.github.smootheez;

import io.github.smootheez.curseforge.*;
import io.github.smootheez.modrinth.*;
import lombok.*;
import org.gradle.api.*;
import org.gradle.api.file.*;
import org.gradle.api.model.*;

import javax.annotation.*;
import javax.inject.*;
import java.util.*;

/**
 * Gradle extension that configures publishing options for Minecraft mods
 * to platforms such as CurseForge and Modrinth.
 * <p>
 * This extension exposes user-defined metadata (version, changelog, loaders, etc.),
 * file selection, and platform-specific configuration blocks.
 * It is registered in the Gradle build script, allowing developers to declare
 * publishing settings directly in their project configuration.
 */
@Getter
@Setter
public class McModPublisherExtension {

    /**
     * Optional display name for the uploaded file.
     * If {@code null}, the platform will use its own default naming logic.
     */
    @Nullable
    private String displayName;

    /**
     * Version of the mod being published.
     * Must be non-null and is typically used as the uploaded file version.
     */
    private String version;

    /**
     * Release channel for publication (e.g., {@code "release"}, {@code "beta"}, {@code "alpha"}).
     * Defaults to {@code "release"}.
     */
    private String releaseType = "release";

    /**
     * Changelog text for the upload. Defaults to an empty string rather than {@code null}
     * to ensure consistent serialization and platform compatibility.
     */
    private String changelog = "";

    /**
     * List of Minecraft game versions that the mod supports (e.g., {@code "1.20.1"}).
     */
    private final List<String> gameVersions = new ArrayList<>();

    /**
     * List of mod loaders supported by this mod (e.g., Fabric, Forge).
     */
    private final List<LoaderType> loaders = new ArrayList<>();

    /**
     * Collection of files to be uploaded, typically containing one mod JAR.
     */
    private final ConfigurableFileCollection files;

    /**
     * CurseForge publishing configuration block.
     */
    private final CurseforgeConfig curseforge;

    /**
     * Modrinth publishing configuration block.
     */
    private final ModrinthConfig modrinth;

    /**
     * Creates the extension and initializes file collection and platform configurations.
     *
     * @param project the Gradle project to attach file collections to
     * @param objects Gradle object factory for constructing nested configuration objects
     */
    @Inject
    public McModPublisherExtension(Project project, ObjectFactory objects) {
        this.files = project.files();
        this.curseforge = objects.newInstance(CurseforgeConfig.class, objects);
        this.modrinth = objects.newInstance(ModrinthConfig.class, objects);
    }

    /**
     * Applies user configuration to the Modrinth publishing settings.
     *
     * @param action a configuration action providing access to {@link ModrinthConfig}
     */
    public void modrinth(Action<ModrinthConfig> action) {
        action.execute(modrinth);
    }

    /**
     * Applies user configuration to the CurseForge publishing settings.
     *
     * @param action a configuration action providing access to {@link CurseforgeConfig}
     */
    public void curseforge(Action<CurseforgeConfig> action) {
        action.execute(curseforge);
    }
}

