package io.github.smootheez.curseforge;

import com.google.gson.reflect.*;
import io.github.smootheez.*;
import io.github.smootheez.exception.*;
import okhttp3.*;
import org.gradle.api.*;

import java.io.*;
import java.util.*;

public class CurseforgePublisher extends Publisher {
    private static final String UPLOAD_URL = "https://minecraft.curseforge.com/api/projects/%s/upload-file";
    private static final String GAME_VERSIONS_URL = "https://minecraft.curseforge.com/api/api/game/versions";

    public CurseforgePublisher(Project project, McModPublisherExtension extension, OkHttpClient client) {
        super(project, extension, client);
    }

    @Override
    public void publish() {
        var curseforge = extension.getCurseforge();
        var token = curseforge.getToken().trim();
        var projectId = curseforge.getProjectId().trim();
        var iterator = extension.getFiles().getFiles().iterator();

        var desiredMcVersions = extension.getGameVersions(); // ["1.20.1"]
        var desiredLoaders = extension.getLoaders().stream()
                .map(this::mapLoaderToCF)
                .toList();

        var desiredEnvs = curseforge.getEnvironmentType().stream()
                .map(this::mapEnvironmentToCF)
                .toList();

        var validGameVersions = fetchGameVersions().stream()
                .filter(tag ->
                        desiredMcVersions.contains(tag.name()) ||
                                desiredLoaders.contains(tag.name()) ||
                                desiredEnvs.contains(tag.name())
                )
                .map(GameVersionTag::id)
                .toList();

        if (validGameVersions.isEmpty()) {
            project.getLogger().error("No valid game versions found. Please check your game versions in the configuration.");
            return;
        }

        var dependencyList = curseforge.getDependencies().stream().map(
                dep -> ProjectsMetadata.builder()
                        .projectId(dep.getProjectId())
                        .relationType(dep.getRelationType())
                        .build()
        ).toList();

        var metadata = curseforgeMetadata(curseforge, validGameVersions, dependencyList);
        publishingToCurseforge(metadata, iterator, projectId, token);
    }

    private String mapLoaderToCF(LoaderType loader) {
        return switch (loader) {
            case FABRIC -> "Fabric";
            case QUILT -> "Quilt";
            case FORGE -> "Forge";
            case NEOFORGE -> "NeoForge";
        };
    }

    private String mapEnvironmentToCF(EnvironmentType env) {
        return switch (env) {
            case CLIENT -> "Client";
            case SERVER -> "Server";
        };
    }

    private void publishingToCurseforge(CurseforgeMetadata metadata, Iterator<File> iterator, String projectId, String token) {
        var multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        multipartBuilder.addFormDataPart(Constants.METADATA,
                null,
                RequestBody.create(GSON.toJson(metadata), MediaType.parse(Constants.MEDIA_TYPE_JSON))
        );

        var file = iterator.next();
        multipartBuilder.addFormDataPart(
                "file",
                file.getName(),
                RequestBody.create(file, MediaType.parse(Constants.MEDIA_TYPE_JAR))
        );

        var requestBody = multipartBuilder.build();
        var request = new Request.Builder()
                .url(String.format(UPLOAD_URL, projectId))
                .header("X-Api-Key", token)
                .header("User-Agent", Constants.USER_AGENT)
                .post(requestBody)
                .build();

        try (var response = client.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw new FailedFileUploadException("Failed to upload mod to Curseforge: " + response.code() + " - " + response.message());

            project.getLogger().lifecycle("Successfully uploaded mod to Curseforge!");
        } catch (IOException e) {
            throw new FailedFileUploadException("Failed to upload mod to Curseforge: " + e.getMessage());
        }
    }

    private CurseforgeMetadata curseforgeMetadata(CurseforgeConfig curseforge, List<Integer> validGameVersions, List<ProjectsMetadata> dependencyList) {
        return CurseforgeMetadata.builder()
                .changelog(extension.getChangelog())
                .changelogType(curseforge.getChangelogType())
                .displayName(extension.getDisplayName())
                .gameVersions(validGameVersions)
                .releaseType(extension.getReleaseType())
                .isMarkedForManualRelease(curseforge.isManualRelease())
                .relations(new Projects(dependencyList))
                .build();
    }

    private List<GameVersionTag> fetchGameVersions() {
        var request = new Request.Builder()
                .url(GAME_VERSIONS_URL)
                .get()
                .build();

        try (var response = client.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw new FailedFetchGameVersionsException("Failed to fetch game versions: " + response.code() + " - " + response.message());

            var responseBody = response.body().string();
            return GSON.fromJson(responseBody, new TypeToken<List<GameVersionTag>>() {
            }.getType());
        } catch (IOException e) {
            throw new FailedFetchGameVersionsException("Failed to fetch game versions" + e.getMessage());
        }
    }
}
