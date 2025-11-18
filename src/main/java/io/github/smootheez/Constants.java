package io.github.smootheez;

import java.time.*;
import java.util.*;

public class Constants {

    private Constants() {}

    public static final String USER_AGENT = "Smootheez/Mc-Mod-Publisher/1.0-SNAPSHOT";
    public static final String MEDIA_TYPE_JSON = "application/json";
    public static final String MEDIA_TYPE_JAR = "application/java-archive";
    public static final Set<String> VALID_RELEASE_TYPE = Set.of("release", "beta", "alpha");
    public static final Duration TIMEOUT = Duration.ofSeconds(20);
    public static final String DATA = "data";
    public static final String METADATA = "metadata";
}
