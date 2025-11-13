package io.github.smootheez.modrinth;

import com.google.gson.*;
import com.google.gson.reflect.*;
import io.github.smootheez.*;
import io.github.smootheez.exception.*;
import lombok.*;
import lombok.extern.slf4j.*;
import okhttp3.*;
import org.gradle.api.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

@Slf4j
public class ModrinthPublisher extends McModPublisher {
    private static final String UPLOAD_URL = "https://api.modrinth.com/v2/version";
    private static final String GAME_VERSION_URL = "https://api.modrinth.com/v2/tag/game_version";

    public ModrinthPublisher(Project project, McModPublisherExtension extension, OkHttpClient client) {
        super(project, extension, client);
    }

    @Override
    public void publish() {
        var modrinth = extension.getModrinth();
        var token = modrinth.getToken().trim();
        var projectId = modrinth.getProjectId().trim();
        var files = extension.getFiles();

        var dependecyList = modrinth.getDependencies().stream().map(
                dep -> DependencyMetadata.builder()
                        .projectId(dep.getProjectId())
                        .dependencyType(dep.getDependencyType())
                        .build()
        ).toList();

        var validGameVersions = fetchGameVersions().stream()
                .map(GameVersionTag::version).filter(extension.getGameVersions()::contains).toList();

        if (validGameVersions.isEmpty()) {
            project.getLogger().error("No valid game versions found. Please check your game versions in the configuration.");
            return;
        }

        String releaseType = extension.getReleaseType();
        if (!Constants.VALID_RELEASE_TYPE.contains(releaseType)) {
            project.getLogger().error("Invalid release type. Please check your configuration.");
            return;
        }

        List<String> filePartNames = IntStream.range(0, files.getFiles().size())
                .mapToObj(i -> i == 0 ? "file" : "file_" + i)
                .toList();

        var metadata = ModrinthMetadata.builder()
                .projectId(projectId)
                .name(extension.getDisplayName())
                .versionNumber(extension.getVersion())
                .changelog(extension.getChangelog())
                .gameVersions(validGameVersions)
                .loaders(extension.getLoaders())
                .releaseChannel(releaseType)
                .featured(modrinth.isFeatured())
                .status(modrinth.getStatus())
                .dependencies(dependecyList)
                .fileParts(filePartNames)
                .build();

        var multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        multipartBuilder.addFormDataPart(Constants.METADATA,
                null,
                RequestBody.create(GSON.toJson(metadata), MediaType.parse(Constants.MEDIA_TYPE_JSON))
        );

        files.getFiles().forEach(file ->
                multipartBuilder.addFormDataPart(
                        filePartNames.get(new ArrayList<>(files.getFiles()).indexOf(file)),
                        file.getName(),
                        RequestBody.create(file, MediaType.parse(Constants.MEDIA_TYPE_JAR))
                )
        );

        var requestBody = multipartBuilder.build();
        var request = new Request.Builder()
                .url(UPLOAD_URL)
                .header("Authorization", token)
                .header("User-Agent", Constants.USER_AGENT)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw new FailedFileUploadException("Failed to upload mod to Modrinth: " + response.code() + " - " + response.message());

            project.getLogger().lifecycle("Successfully uploaded mod to Modrinth!");
        } catch (IOException e) {
            throw new FailedFileUploadException("Failed to upload mod to Modrinth: " + e.getMessage());
        }
    }

    List<GameVersionTag> fetchGameVersions() {
        var request = new Request.Builder()
                .url(GAME_VERSION_URL)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw new FailedFetchGameVersionsException("Failed to fetch game versions: " + response.code() + " - " + response.message());

            String responseBody = response.body().string();
            return GSON.fromJson(responseBody, new TypeToken<List<GameVersionTag>>() {
            }.getType());
        } catch (IOException e) {
            throw new FailedFetchGameVersionsException("Failed to fetch game versions" + e.getMessage());
        }
    }
}
