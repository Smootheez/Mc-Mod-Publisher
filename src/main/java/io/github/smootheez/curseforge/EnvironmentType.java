package io.github.smootheez.curseforge;

import com.google.gson.annotations.*;

/**
 * Defines the runtime environment targets for a mod when publishing
 * to platforms that distinguish between client-side and server-side compatibility.
 * <p>
 * The values are annotated with {@link SerializedName} to ensure correct
 * JSON serialization when communicating with external APIs such as CurseForge.
 */
public enum EnvironmentType {

    /**
     * Indicates that the mod is intended to run on the client environment.
     * This typically includes features such as rendering, UI components,
     * client-side event handling, or visuals that are not required on servers.
     * Serialized as {@code "client"}.
     */
    @SerializedName("client")
    CLIENT,

    /**
     * Indicates that the mod is intended to run on a dedicated or integrated server.
     * Server-side mods usually contain game logic, world management,
     * or backend systems that do not rely on client rendering.
     * Serialized as {@code "server"}.
     */
    @SerializedName("server")
    SERVER
}

