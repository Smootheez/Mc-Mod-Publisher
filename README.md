# 📦 Minecraft Mod Publisher — Configuration Overview

**Note:** This project is currently still in early development, if you found issues or have suggestions, please open an issue on GitHub.

---

This plugin provides a unified Gradle DSL for publishing Minecraft mods to **CurseForge** and **Modrinth**.
Below is a complete reference for all configuration parameters.

---

## 📘 Configuration Reference

### General Settings

| Parameter      | Value / Example                | Required | Description                                                        |
|----------------|--------------------------------|----------|--------------------------------------------------------------------|
| `displayName`  | Smooth Expansion v1.2.0        | Optional | Friendly name shown on platforms. Defaults to the output filename. |
| `version`      | 1.2.0                          | **Yes**  | Version number for the upload.                                     |
| `releaseType`  | release                        | Optional | Release channel (`release`, `beta`, `alpha`).                      |
| `changelog`    | Reads from `CHANGELOG.md`      | Optional | Changelog text; empty string if not provided.                      |
| `files`        | Output of `jar` task           | **Yes**  | Files to upload (normally your compiled JAR).                      |
| `gameVersions` | 1.20.1, 1.19.4                 | **Yes**  | Minecraft versions supported by this release.                      |
| `loaders`      | Forge, NeoForge, Fabric, Quilt | **Yes**  | Mod loaders supported by the build.                                |

---

### 🔶 CurseForge Configuration

| Parameter         | Value / Example  | Required | Description                                            |
|-------------------|------------------|----------|--------------------------------------------------------|
| `token`           | curseforge-token | **Yes**  | API token (store in environment variables for safety). |
| `projectId`       | 123456           | **Yes**  | Your CurseForge project ID.                            |
| `changelogType`   | MARKDOWN         | Optional | Format of the changelog text.                          |
| `environmentType` | Client, Server   | Optional | Target environments supported by your mod.             |
| `isManualRelease` | false            | Optional | If true, the upload requires manual approval.          |

#### CurseForge Dependencies

| Type           | Example Slug      | Description                            |
|----------------|-------------------|----------------------------------------|
| `required`     | required-slug     | Hard dependencies required at runtime. |
| `optional`     | optional-slug     | Optional enhancements.                 |
| `incompatible` | incompatible-slug | Known incompatible mods.               |
| `embedded`     | embedded-slug     | Dependencies packaged inside your mod. |
| `tool`         | tool-slug         | Tools or helper libraries.             |

---

### 🟩 Modrinth Configuration

| Parameter    | Value / Example | Required | Description                                                             |
|--------------|-----------------|----------|-------------------------------------------------------------------------|
| `token`      | modrinth-token  | **Yes**  | API token (should be stored as an environment variable).                |
| `projectId`  | abcd1234        | **Yes**  | Your Modrinth project ID.                                               |
| `isFeatured` | true            | Optional | Marks this version as featured on Modrinth.                             |
| `status`     | listed          | Optional | Listing state (`listed`, `archived`, `draft`, `unlisted`, `scheduled`). |

#### Modrinth Dependencies

| Type           | Example ID | Description                                   |
|----------------|------------|-----------------------------------------------|
| `required`     | P7dR8mSH   | Required runtime dependencies.                |
| `optional`     | 4Kc3yK8M   | Optional or recommended mods.                 |
| `incompatible` | ET0f402o   | Mods that cannot be used with this mod.       |
| `embedded`     | AANQXDDX   | Libraries bundled directly inside your build. |

---

# 🧩 Example Configuration

```kotlin
mcModPublisher {
    // The name that will appear on distribution platforms (optional).
    // If omitted, the jar filename is used instead.
    displayName = "Smooth Expansion v1.2.0"

    // The version of your mod being published (required).
    version = "1.2.0"

    // Release channel for the upload: 'release', 'beta', or 'alpha'.
    releaseType = "release" // default setting

    // Loads the changelog content directly from CHANGELOG.md.
    changelog = file("CHANGELOG.md").readText() // if not specified it will send empty string to the API

    // Specifies the files to be uploaded. This example uses the output of the 'remapJar' task.
    files.from(tasks.named("remapJar"))

    // Sets supported Minecraft versions (required).
    gameVersions.addAll(listOf("1.20.1", "1.19.4"))

    // Specifies mod loaders supported by this build (e.g., Forge, Fabric) (required).
    loaders.addAll(listOf(LoaderType.FORGE, LoaderType.NEOFORGE, LoaderType.FABRIC, LoaderType.QUILT))

    // CurseForge upload configuration.
    curseforge {
        // API token for CurseForge uploads (replace with env variable in real setups) (required).
        token = "curseforge-token"

        // ID of the project on CurseForge (required).
        projectId = "123456"

        // Format of the changelog file you uploaded.
        changelogType = ChangelogType.MARKDOWN // default plugin setting to match modrinth changelog type

        // Specifies whether the mod targets client, server.
        environmentType = listOf(EnvironmentType.CLIENT, EnvironmentType.SERVER)

        // Whether the mod should require manual approval before publishing.
        isManualRelease = false

        // Dependency metadata for CurseForge.
        dependencies {
            // Hard requirements — the mod cannot run without these.
            required("required-slug")

            // Optional mods that enhance or add extra features.
            optional("optional-slug")

            // Mods known to be incompatible with this one.
            incompatible("incompatible-slug")

            // Dependencies bundled inside your jar.
            embedded("embedded-slug")

            // Tools, like libraries or development helpers.
            tool("tool-slug")
        }
    }

    // Modrinth upload configuration.
    modrinth {
        // API token for Modrinth (use env variable for security) (required).
        token = "modrinth-token"

        // ID of the project on Modrinth (required).
        projectId = "abcd1234"

        // Marks the version as a featured release on Modrinth.
        isFeatured = true

        // Determines listing visibility: 'listed', 'archived', 'draft', 'unlisted', 'scheduled'
        status = "listed"

        // Dependency metadata for Modrinth.
        dependencies {
            // Required mods needed to run this project.
            required("P7dR8mSH")

            // Optional mods that enhance functionality.
            optional("4Kc3yK8M")

            // Mods that conflict with this project.
            incompatible("ET0f402o")

            // Bundled dependencies included within your jar.
            embedded("AANQXDDX")
        }
    }
}
```

---

# 🚀 Installation Guide

Installation now works like any standard Gradle plugin published on the Plugin Portal.

---

## 1. Add the plugin (no extra repositories required)

### **Kotlin DSL — `build.gradle.kts`**

```kotlin
plugins {
    id("io.github.smootheez.mc-mod-publisher") version "1.0.0"
}
```

### **Groovy DSL — `build.gradle`**

```groovy
plugins {
    id 'io.github.smootheez.mc-mod-publisher' version '1.0.0'
}
```

> Replace `"1.0.0"` with the latest published version.

---

## 2. (Optional) If you're using a custom plugin version repository

Only needed if you want to override the release with a locally installed build.

### **Kotlin DSL — `settings.gradle.kts`**

```kotlin
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal() // optional
    }
}
```

### **Groovy DSL — `settings.gradle`**

```groovy
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal() // optional
    }
}
```

---

# 🧪 Publishing Commands

Run from inside your mod project:

```bash
./gradlew publishModToAll
./gradlew publishModToModrinth
./gradlew publishModToCurseforge
```
