plugins {
    id("java")
    id("java-gradle-plugin")          // Enables Gradle plugin development
    id("maven-publish")               // Optional: publishing to Maven repo
}

group = "io.github.smootheez"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val gsonVersion = "2.13.2"
val lombokVersion = "1.18.42"
val mockitoVersion = "5.20.0"
val okhttpVersion = "5.3.0"

dependencies {
    implementation(gradleApi())        // Provides Gradle APIs
    implementation(localGroovy())      // Only if needed (not mandatory)

    implementation("com.google.code.gson:gson:${gsonVersion}")
    implementation("com.squareup.okhttp3:okhttp:${okhttpVersion}")

    compileOnly("org.projectlombok:lombok:${lombokVersion}")
    annotationProcessor("org.projectlombok:lombok:${lombokVersion}")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:${mockitoVersion}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testCompileOnly("org.projectlombok:lombok:${lombokVersion}")
    testAnnotationProcessor("org.projectlombok:lombok:${lombokVersion}")
}

gradlePlugin {
    plugins {
        create("McModPublisher") {
            id = "io.github.smootheez.mc-mod-publisher"            // plugin id
            implementationClass = "io.github.smootheez.McModPublisherPlugin"   // main plugin class
            displayName = "Minecraft Mod Publisher"
            description = "Gradle plugin to publish Minecraft mods to various platforms."
            tags = listOf("minecraft", "mod", "publisher", "curseforge", "modrinth")
        }
    }
}

tasks.test {
    useJUnitPlatform()
}
