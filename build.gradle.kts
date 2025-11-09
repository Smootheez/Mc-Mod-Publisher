plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
}

group = "io.github.smootheez"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val kotlinSerializationVersion = "1.9.0"
val okhttpVersion = "5.2.1"

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${kotlinSerializationVersion}")
    implementation("com.squareup.okhttp3:okhttp:${okhttpVersion}")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

gradlePlugin {
    plugins {
        create("mcModPublisher") {
            id = "io.github.smootheez.mc-mod-publisher"
            implementationClass = "io.github.smootheez.McModPublisherPlugin"
            displayName = "Minecraft Mod Publisher Plugin"
            description = "A Gradle plugin to automate publishing Minecraft mods"
        }
    }
}
