package io.github.smootheez.curseforge;

import com.google.gson.reflect.*;
import io.github.smootheez.*;
import io.github.smootheez.exception.*;
import okhttp3.*;
import org.gradle.api.*;

import java.io.*;
import java.util.*;

/**
 * Handles publishing mod files to CurseForge using the CurseForge upload API.
 * <p>
 * This publisher is responsible for:
 * <ul>
 *     <li>Resolving configured game versions, loaders, and environments</li>
 *     <li>Constructing metadata for the upload request</li>
 *     <li>Building and sending a multipart upload request to CurseForge</li>
 *     <li>Logging responses and raising exceptions for failed uploads</li>
 * </ul>
 */
public class CurseforgePublisher extends Publisher {

    /** Upload endpoint for submitting new files to a CurseForge project. */
    private static final String UPLOAD_URL = "https://minecraft.curseforge.com/api/projects/%s/upload-file";

    /** Endpoint for retrieving CurseForge's game version metadata. */
    private static final String GAME_VERSIONS_URL = "https://minecraft.curseforge.com/api/game/versions";

    /**
     * Constructs a new CurseForge publisher instance.
     *
     * @param project   the Gradle project
     * @param extension the plugin extension containing user configuration
     * @param client    the HTTP client used for API communication
     */
    public CurseforgePublisher(Project project, McModPublisherExtension extension, OkHttpClient client) {
        super(project, extension, client);
    }

    /**
     * Publishes all configured files to CurseForge.
     * <p>
     * Steps performed include:
     * <ol>
     *     <li>Reading configuration (token, project ID, load targets)</li>
     *     <li>Resolving valid CurseForge game version IDs</li>
     *     <li>Constructing upload metadata</li>
     *     <li>Sending each file to CurseForge using a multipart upload</li>
     * </ol>
     * If no valid game versions are found, the publish process is aborted.
     */
    @Override
    public void publish() {
        var curseforge = extension.getCurseforge();
        var token = curseforge.getToken().trim();
        var projectId = curseforge.getProjectId().trim();
        var iterator = extension.getFiles().getFiles().iterator();

        var desiredMcVersions = extension.getGameVersions();
        var desiredLoaders = extension.getLoaders().stream()
                .map(this::mapLoaderToCF)
                .toList();

        var desiredEnvs = curseforge.getEnvironmentType().stream()
                .map(this::mapEnvironmentToCF)
                .toList();

        var validGameVersions = fetchGameVersions(token).stream()
                .filter(tag -> {
                    var type = tag.gameVersionTypeId();
                    return type == 77784 || type == 68441 || type == 75208;
                })
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

        var dependencyList = curseforge.getDependencies().stream()
                .map(dep -> ProjectsMetadata.builder()
                        .slug(dep.getSlug())
                        .relationType(dep.getRelationType())
                        .build())
                .toList();

        var metadata = curseforgeMetadata(curseforge, validGameVersions, dependencyList);
        project.getLogger().lifecycle("Curseforge metadata: " + GSON.toJson(metadata));

        project.getLogger().lifecycle("Publishing to Curseforge...");
        publishingToCurseforge(metadata, iterator, projectId, token);
    }

    /**
     * Maps internal loader types to CurseForge’s corresponding loader name.
     *
     * @param loader the loader type specified in the configuration
     * @return CurseForge’s loader label
     */
    private String mapLoaderToCF(LoaderType loader) {
        return switch (loader) {
            case FABRIC -> "Fabric";
            case QUILT -> "Quilt";
            case FORGE -> "Forge";
            case NEOFORGE -> "NeoForge";
        };
    }

    /**
     * Maps internal environment types to CurseForge’s client/server labels.
     *
     * @param env the environment type
     * @return CurseForge’s environment name
     */
    private String mapEnvironmentToCF(EnvironmentType env) {
        return switch (env) {
            case CLIENT -> "Client";
            case SERVER -> "Server";
        };
    }

    /**
     * Performs the multipart upload to CurseForge.
     *
     * @param metadata  the metadata payload describing the uploaded file
     * @param iterator  iterator over files scheduled for upload
     * @param projectId the CurseForge project ID
     * @param token     authentication token
     */
    private void publishingToCurseforge(CurseforgeMetadata metadata, Iterator<File> iterator, String projectId, String token) {
        var multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        multipartBuilder.addFormDataPart(
                Constants.METADATA,
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
                .header("X-Api-Token", token)
                .header("User-Agent", Constants.USER_AGENT)
                .post(requestBody)
                .build();

        try (var response = client.newCall(request).execute()) {
            var body = response.body().string();

            project.getLogger().lifecycle("Response received: code=" + response.code() + " message=" + response.message());
            project.getLogger().lifecycle("Response body: " + body);

            if (!response.isSuccessful()) {
                throw new FailedFileUploadException(
                        "Failed to upload mod to Curseforge: " +
                                response.code() + " - " + response.message() + " - BODY: " + body
                );
            }

            project.getLogger().lifecycle("Successfully uploaded mod to Curseforge!");
        } catch (IOException e) {
            throw new FailedFileUploadException("Failed to upload mod to Curseforge: " + e.getMessage());
        }
    }

    /**
     * Builds a {@link CurseforgeMetadata} instance based on configuration and valid game versions.
     *
     * @param curseforge        CurseForge-specific configuration
     * @param validGameVersions resolved CurseForge game version IDs
     * @param dependencyList    resolved dependency metadata
     * @return a fully constructed CurseForge metadata object
     */
    private CurseforgeMetadata curseforgeMetadata(
            CurseforgeConfig curseforge,
            List<Integer> validGameVersions,
            List<ProjectsMetadata> dependencyList
    ) {
        var builder = CurseforgeMetadata.builder()
                .changelog(extension.getChangelog())
                .changelogType(curseforge.getChangelogType())
                .displayName(extension.getDisplayName())
                .gameVersions(validGameVersions)
                .releaseType(extension.getReleaseType())
                .isMarkedForManualRelease(curseforge.isManualRelease());

        if (!dependencyList.isEmpty()) {
            builder.relations(new Projects(dependencyList));
        }

        return builder.build();
    }

    /**
     * Fetches the full list of CurseForge game version tags.
     *
     * @param token the API token for authentication
     * @return list of available game version tags
     * @throws FailedFetchGameVersionsException if the request fails
     */
    private List<GameVersionTag> fetchGameVersions(String token) {
        var request = new Request.Builder()
                .url(GAME_VERSIONS_URL)
                .header("X-Api-Token", token)
                .header("User-Agent", Constants.USER_AGENT)
                .get()
                .build();

        try (var response = client.newCall(request).execute()) {
            var body = response.body().string();

            if (!response.isSuccessful()) {
                throw new FailedFetchGameVersionsException(
                        "Failed to fetch game versions: " +
                                response.code() + " - " + response.message() + " - BODY: " + body
                );
            }

            return GSON.fromJson(body, new TypeToken<List<GameVersionTag>>() {}.getType());
        } catch (IOException e) {
            throw new FailedFetchGameVersionsException("Failed to fetch game versions" + e.getMessage());
        }
    }
}

