package io.github.smootheez;

import io.github.smootheez.curseforge.*;
import io.github.smootheez.modrinth.*;
import okhttp3.*;
import org.gradle.api.*;

public class McModPublisherPlugin implements Plugin<Project> {
    private static final String PUBLISHER = "publisher";

    @Override
    public void apply(Project project) {
        var extension = project.getExtensions().create("mcModPublisher", McModPublisherExtension.class);

        var client = new OkHttpClient.Builder().callTimeout(Constants.TIMEOUT).build();
        project.getTasks().register("publishModToModrinth", task -> {
            task.setGroup(PUBLISHER);
            task.setDescription("Uploads the mod to Modrinth");
            task.doLast(t -> new ModrinthPublisher(project, extension, client).publish());
        });

        project.getTasks().register("publishModToCurseforge", task -> {
            task.setGroup(PUBLISHER);
            task.setDescription("Uploads the mod to Curseforge");
            task.doLast(t -> new CurseforgePublisher(project, extension, client).publish());
        });
    }
}
