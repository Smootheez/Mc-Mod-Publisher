package io.github.smootheez;

import java.time.*;
import java.util.*;

/**
 * Holds application-wide constant values used throughout the Mod Publisher service.
 * This class is non-instantiable and only provides static constant fields.
 */
public final class Constants {

    /** Prevents instantiation of this utility class. */
    private Constants() {}

    /**
     * The User-Agent header sent with HTTP requests made by the publisher.
     * Format: {@code <Author>/<Application>/<Version>}.
     */
    public static final String USER_AGENT = "Smootheez/Mc-Mod-Publisher/1.10";

    /** The JSON media type used for request and response bodies. */
    public static final String MEDIA_TYPE_JSON = "application/json";

    /** The JAR media type used when uploading mod artifacts. */
    public static final String MEDIA_TYPE_JAR = "application/java-archive";

    /** Valid release channels supported for uploads (e.g., release, beta, alpha). */
    public static final Set<String> VALID_RELEASE_TYPE = Set.of("release", "beta", "alpha");

    /** Default request timeout duration for network operations. */
    public static final Duration TIMEOUT = Duration.ofSeconds(20);

    /** JSON key for root-level file data. */
    public static final String DATA = "data";

    /** JSON key for metadata blocks associated with uploads. */
    public static final String METADATA = "metadata";
}
