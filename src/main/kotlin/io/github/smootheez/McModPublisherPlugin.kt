package io.github.smootheez

import org.gradle.api.Plugin
import org.gradle.api.Project

class McModPublisherPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        target.extensions.create("mcModPublisher", McModPublisherExtension::class.java)

        target.tasks.register("publishModToAll") {
            group = "minecraft"
            description = "Publishes your Minecraft mod to a remote repository (placeholder)"
            doLast {
                println("🚀 Publishing Minecraft mod from project: ${project.name}")
            }
        }

        target.tasks.register("publishModToModrinth") {
            group = "minecraft"
            description = "Publishes your Minecraft mod to Modrinth"
            doLast {
                println("🚀 Publishing Minecraft mod to Modrinth from project: ${project.name}")
            }
        }

        target.tasks.register("publishModToCurseForge") {
            group = "minecraft"
            description = "Publishes your Minecraft mod to CurseForge"
            doLast {
                println("🚀 Publishing Minecraft mod to CurseForge from project: ${project.name}")
            }
        }
    }
}