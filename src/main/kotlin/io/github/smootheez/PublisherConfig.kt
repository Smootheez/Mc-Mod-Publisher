package io.github.smootheez

object PublisherConfig {
    const val USER_AGENT = "Smootheez/Mc-Mod-Publisher/1.0-SNAPSHOT"
    const val MEDIA_TYPE_JSON = "application/json"
    const val MEDIA_TYPE_JAR = "application/java-archive"
    val VALID_RELEASE_TYPES = setOf("release", "beta", "alpha")
}