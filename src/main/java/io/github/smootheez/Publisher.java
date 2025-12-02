package io.github.smootheez;

import com.google.gson.*;
import lombok.*;
import okhttp3.*;
import org.gradle.api.*;

/**
 * Base abstraction for all publishing implementations within the
 * Mc-Mod-Publisher Gradle plugin. Concrete subclasses provide the logic
 * necessary to publish a project to a specific platform such as
 * CurseForge, Modrinth, or other distribution services.
 *
 * <p>This class centralizes shared components needed by all publishers:
 * <ul>
 *     <li>{@link Project} — the Gradle project instance</li>
 *     <li>{@link McModPublisherExtension} — user-defined configuration</li>
 *     <li>{@link OkHttpClient} — HTTP client for performing network requests</li>
 *     <li>{@link Gson} — serializer for JSON payloads</li>
 * </ul>
 *
 * <p>Implementations must define the {@link #publish()} method, which
 * encapsulates the complete publishing workflow, including:
 * <ul>
 *     <li>reading configuration</li>
 *     <li>preparing request payloads</li>
 *     <li>executing HTTP calls</li>
 *     <li>handling responses and logging</li>
 * </ul>
 *
 * <p>This class is designed to be extended and used by Gradle tasks or
 * other orchestration components inside the plugin.
 */
@RequiredArgsConstructor
public abstract class Publisher {

    /**
     * The current Gradle project, used for logging and access to build information.
     */
    protected final Project project;

    /**
     * Global extension containing configuration values supplied by users via Gradle DSL.
     */
    protected final McModPublisherExtension extension;

    /**
     * HTTP client used to send HTTP requests to external publishing platforms.
     */
    protected final OkHttpClient client;

    /**
     * Shared Gson instance for serializing and deserializing JSON payloads.
     */
    protected static final Gson GSON = new Gson();

    /**
     * Executes the publishing workflow for a specific platform.
     * <p>
     * Implementations must provide the complete logic required to:
     * <ul>
     *     <li>validate configuration</li>
     *     <li>prepare metadata and artifacts</li>
     *     <li>send requests to the remote API</li>
     *     <li>handle failures and log results</li>
     * </ul>
     *
     * <p>This method is expected to be invoked from a Gradle task or
     * another orchestrator responsible for running publishing actions.
     */
    public abstract void publish();
}

