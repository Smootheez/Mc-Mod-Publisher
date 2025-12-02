package io.github.smootheez.modrinth;

import com.google.gson.reflect.*;
import io.github.smootheez.*;
import io.github.smootheez.exception.*;
import okhttp3.*;
import org.gradle.api.*;
import org.gradle.api.file.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

/**
 * Publishes mod versions to Modrinth using their official API.
 * <p>
 * This publisher is responsible for:
 * <ul>
 *     <li>Validating configuration such as release type, status, and game versions</li>
 *     <li>Fetching available game versions from Modrinth</li>
 *     <li>Building metadata for the version upload</li>
 *     <li>Uploading files and metadata via multipart requests</li>
 * </ul>
 */
public class ModrinthPublisher extends Publisher {

    /** Endpoint for uploading a new version to Modrinth. */
    private static final String UPLOAD_URL = "https://api.modrinth.com/v2/version";

    /** Endpoint for fetching all available game version tags from Modrinth. */
    private static final String GAME_VERSION_URL = "https://api.modrinth.com/v2/tag/game_version";

    /** Supported Modrinth version status values. */
    private static final Set<String> VALID_STATUS = Set.of("listed", "archived", "draft", "unlisted", "scheduled");

    /**
     * Constructs a new {@code ModrinthPublisher}.
     *
     * @param project    the Gradle project instance
     * @param extension  the plugin configuration extension
     * @param client     the HTTP client used for API communication
     */
    public ModrinthPublisher(Project project, McModPublisherExtension extension, OkHttpClient client) {
        super(project, extension, client);
    }

    /**
     * Executes the publishing process to Modrinth.
     * <p>
     * This includes:
     * <ul>
     *     <li>Validating configuration</li>
     *     <li>Fetching supported game versions from Modrinth</li>
     *     <li>Constructing metadata</li>
     *     <li>Sending the multipart request containing metadata and mod files</li>
     * </ul>
     * Logs and aborts on invalid configuration or missing versions.
     */
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

        var releaseType = extension.getReleaseType();
        if (!Constants.VALID_RELEASE_TYPE.contains(releaseType)) {
            project.getLogger().error("Invalid release type. Please check your configuration.");
            return;
        }

        if (!VALID_STATUS.contains(modrinth.getStatus())) {
            project.getLogger().error("Invalid status. Please check your configuration.");
            return;
        }

        var filePartNames = IntStream.range(0, files.getFiles().size())
                .mapToObj(i -> i == 0 ? "file" : "file_" + i)
                .toList();

        var metadata = modrinthMetadata(projectId, validGameVersions, releaseType, modrinth, dependecyList, filePartNames);
        project.getLogger().lifecycle("Metadata JSON: " + GSON.toJson(metadata));

        project.getLogger().lifecycle("Publishing to Modrinth...");
        publishingToModrinth(metadata, files, filePartNames, token);
    }

    /**
     * Sends a multipart upload request to Modrinth containing metadata and all mod files.
     *
     * @param metadata       the JSON metadata describing this version
     * @param files          the mod files to upload
     * @param filePartNames  generated field names for each file part
     * @param token          Modrinth API authorization token
     * @throws FailedFileUploadException if the upload request fails
     */
    private void publishingToModrinth(ModrinthMetadata metadata,
                                      ConfigurableFileCollection files,
                                      List<String> filePartNames,
                                      String token) {

        var multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        multipartBuilder.addFormDataPart(
                Constants.DATA,
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

        try (var response = client.newCall(request).execute()) {
            var body = response.body().string();

            if (!response.isSuccessful()) {
                project.getLogger().lifecycle("Upload failed. Response body: " + body);

                throw new FailedFileUploadException(
                        "Failed to upload mod to Modrinth: " +
                                response.code() + " - " +
                                response.message() + " - BODY: " + body
                );
            }

            project.getLogger().lifecycle("Successfully uploaded mod to Modrinth!");
        } catch (IOException e) {
            throw new FailedFileUploadException("Failed to upload mod to Modrinth: " + e.getMessage());
        }
    }

    /**
     * Builds a {@link ModrinthMetadata} instance from configuration values.
     *
     * @param projectId        the Modrinth project ID
     * @param validGameVersions game versions confirmed valid by Modrinth
     * @param releaseType      version release channel (e.g., "release", "beta", "alpha")
     * @param modrinth         Modrinth-specific configuration
     * @param dependecyList    dependencies declared for this version
     * @param filePartNames    names for multipart file sections
     * @return fully populated {@code ModrinthMetadata}
     */
    private ModrinthMetadata modrinthMetadata(String projectId,
                                              List<String> validGameVersions,
                                              String releaseType,
                                              ModrinthConfig modrinth,
                                              List<DependencyMetadata> dependecyList,
                                              List<String> filePartNames) {

        return ModrinthMetadata.builder()
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
    }

    /**
     * Fetches the full list of game version tags from Modrinth.
     *
     * @return list of {@link GameVersionTag} objects
     * @throws FailedFetchGameVersionsException if Modrinth returns an error or the request fails
     */
    private List<GameVersionTag> fetchGameVersions() {
        var request = new Request.Builder()
                .url(GAME_VERSION_URL)
                .get()
                .build();

        try (var response = client.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw new FailedFetchGameVersionsException(
                        "Failed to fetch game versions: " +
                                response.code() + " - " + response.message()
                );

            var responseBody = response.body().string();
            return GSON.fromJson(responseBody, new TypeToken<List<GameVersionTag>>() {}.getType());
        } catch (IOException e) {
            throw new FailedFetchGameVersionsException("Failed to fetch game versions" + e.getMessage());
        }
    }
}
