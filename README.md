# The project is still under development...

```kotlin
mcModPublisher {
    displayName = "Smooth Expansion v1.2.0"
    version = "1.2.0"
    changelog = file("CHANGELOG.md").readText()
    files.from(tasks.named("jar"))

    gameVersions.addAll(listOf("1.20.1", "1.19.4"))
    loaders.addAll(listOf(LoaderType.FORGE, LoaderType.FABRIC))

    curseforge {
        token = "curseforge-token"
        projectId = "123456"
        changelogType = ChangelogType.MARKDOWN

        dependencies {
            required(238222)    // e.g. JEI
            optional(312345)    // e.g. Some integration mod
            embedded(412999)
        }
    }

    modrinth {
        token = "modrinth-token"
        projectId = "abcd1234"
        featured = true

        dependencies {
            required("P7dR8mSH") // JEI
            optional("4Kc3yK8M") // TOP
            incompatible("ET0f402o") // WTH
            embedded("AANQXDDX") // REI
        }
    }
}
```