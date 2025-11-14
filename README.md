# The project is still under development...

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

    // Specifies which files will be uploaded; uses the built jar from the 'jar' task (required).
    files.from(tasks.named("jar"))

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
            required(123456)

            // Optional mods that enhance or add extra features.
            optional(789012)

            // Mods known to be incompatible with this one.
            incompatible(345678)

            // Dependencies bundled inside your jar.
            embedded(901234)

            // Tools, like libraries or development helpers.
            tool(567890)
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