package io.github.smootheez;

import io.github.smootheez.curseforge.*;
import io.github.smootheez.modrinth.*;
import okhttp3.*;
import org.gradle.api.*;

/**
 * Gradle plugin that registers tasks and configuration required to publish
 * Minecraft mods to Modrinth and CurseForge.
 * <p>
 * When applied, this plugin creates the {@code mcModPublisher} extension,
 * initializes an HTTP client used by publishers, and registers tasks for
 * uploading mods to individual or multiple platforms.
 */
public class McModPublisherPlugin implements Plugin<Project> {

    /** Name of the task group under which all publishing tasks are organized. */
    private static final String PUBLISHER = "publisher";

    /**
     * Applies the plugin to the target Gradle project by registering the extension
     * and creating the publishing tasks.
     *
     * @param project the Gradle project to configure
     */
    @Override
    public void apply(Project project) {

        // Create extension holding all publisher configuration
        var extension = project.getExtensions()
                .create("mcModPublisher", McModPublisherExtension.class);

        // Shared OkHttp client with configured timeout
        var client = new OkHttpClient.Builder()
                .callTimeout(Constants.TIMEOUT)
                .build();

        // Task: Publish to both Modrinth and CurseForge
        project.getTasks().register("publishModToAll", task -> {
            task.setGroup(PUBLISHER);
            task.setDescription("Uploads the mod to all platforms");
            task.doLast(t -> {
                new ModrinthPublisher(project, extension, client).publish();
                new CurseforgePublisher(project, extension, client).publish();
            });
        });

        // Task: Publish only to Modrinth
        project.getTasks().register("publishModToModrinth", task -> {
            task.setGroup(PUBLISHER);
            task.setDescription("Uploads the mod to Modrinth");
            task.doLast(t -> new ModrinthPublisher(project, extension, client).publish());
        });

        // Task: Publish only to CurseForge
        project.getTasks().register("publishModToCurseforge", task -> {
            task.setGroup(PUBLISHER);
            task.setDescription("Uploads the mod to Curseforge");
            task.doLast(t -> new CurseforgePublisher(project, extension, client).publish());
        });
    }
}

