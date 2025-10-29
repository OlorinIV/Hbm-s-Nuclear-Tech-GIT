
plugins {
    id("com.gtnewhorizons.gtnhconvention")
}

val modMainVersion = properties["mod_version"]
val modBuildNumber = properties["mod_build_number"]
val modBuildNumberSub = properties["mod_build_number_sub"]

val modVersion = "${modMainVersion}_X$modBuildNumber.$modBuildNumberSub"

val credits: String by project

val customArchiveBaseName: String by project
val modVersionInFileName = "X$modBuildNumber.$modBuildNumberSub"

tasks.processResources.configure {
    filesMatching("mcmod.info") {
        expand(mapOf(
            "version" to modVersion,
            "credits" to credits)
        )
    }
}

tasks.jar.configure {
    archiveFileName = "$customArchiveBaseName-$modVersionInFileName-dev.jar"
}

tasks.reobfJar.configure {
    archiveFileName = "$customArchiveBaseName-$modVersionInFileName.jar"
}

tasks.sourcesJar.configure {
    archiveFileName = "$customArchiveBaseName-$modVersionInFileName-sources.jar"
}

//If you do not like the dev jar I think I could move this to somewhere else...
//Thanks to GitHub Copilot it works

tasks.register("moveOutput") {
    dependsOn(tasks.named("sourcesJar"))

    doLast {
        copy {
            from(layout.buildDirectory.dir("/libs")) {
                include(
                    "$customArchiveBaseName-$modVersionInFileName-dev.jar",
                    "$customArchiveBaseName-$modVersionInFileName-sources.jar"
                )
            }

            into(layout.buildDirectory.dir("/otherBuildArtifacts"))
        }

        delete(fileTree(layout.buildDirectory.dir("libs").get().asFile) {
            include(
                "${customArchiveBaseName}-${modVersionInFileName}-dev.jar",
                "${customArchiveBaseName}-${modVersionInFileName}-sources.jar"
            )
        })
    }
}

tasks.named("build") {
    finalizedBy(tasks.named("moveOutput"))
}
