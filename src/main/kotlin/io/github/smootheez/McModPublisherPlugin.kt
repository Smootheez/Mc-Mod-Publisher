package io.github.smootheez

import io.github.smootheez.curseforge.CurseforgePublisher
import io.github.smootheez.modrinth.ModrinthPublisher
import org.gradle.api.Plugin
import org.gradle.api.Project

class McModPublisherPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions.create("mcModPublisher", McModPublisherExtension::class.java)

        target.tasks.register("publishModToAll") {
            group = "minecraft"
            description = "Publishes your Minecraft mod to a remote repository (placeholder)"
            doLast {
                println("\uD83D\uDE80  Publishing Minecraft mod from project: ${project.name}")
                ModrinthPublisher(project, extension).publish()
                CurseforgePublisher(project, extension).publish()
            }
        }

        target.tasks.register("publishModToModrinth") {
            group = "minecraft"
            description = "Publishes your Minecraft mod to Modrinth"
            doLast {
                ModrinthPublisher(project, extension).publish()
            }
        }

        target.tasks.register("publishModToCurseForge") {
            group = "minecraft"
            description = "Publishes your Minecraft mod to CurseForge"
            doLast {
                CurseforgePublisher(project, extension).publish()
            }
        }
    }
}