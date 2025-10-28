import kotlinx.coroutines.newSingleThreadContext
import org.gradle.api.internal.ConventionTask

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
            "modVersion" to modVersion,
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
    finalizedBy("moveOutput")
}

//If you do not like the dev jar I think I could move this to somewhere else...
//Proven to be useless as when called after builds this won't run properly, but call this task separately it works almost fine

tasks.register<Copy>("moveOutput") {
    copy {
        from(layout.buildDirectory.dir("/libs")) {
            include("$customArchiveBaseName-$modVersionInFileName-dev.jar", "$customArchiveBaseName-$modVersionInFileName-sources.jar")
        }

        into(layout.buildDirectory.dir("/otherBuildArtifacts"))
    }

    delete(layout.buildDirectory.file("libs/$customArchiveBaseName-$modVersionInFileName-dev.jar"))
    delete(layout.buildDirectory.file("libs/$customArchiveBaseName-$modVersionInFileName-sources.jar"))
}
