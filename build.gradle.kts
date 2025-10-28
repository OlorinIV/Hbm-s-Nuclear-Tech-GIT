
plugins {
    id("com.gtnewhorizons.gtnhconvention")
}

val modMainVersion: String by project
val modBuildNumber: String by project
val modBuildNumberSub: String by project

val modVersion = "${modMainVersion}_X$modBuildNumber.$modBuildNumberSub"
val minecraftVersion: String by project
val modId: String by project
val modName: String by project
val credits: String by project

val customArchiveBaseName: String by project
val modVersionInFileName = "X$modBuildNumber.$modBuildNumberSub"

tasks.processResources.configure {

    filesMatching("mcmod.info") {
        expand(mapOf(
            "version" to modVersion,
            "minecraftVersion" to minecraftVersion,
            "modId" to modId,
            "modName" to modName,
            "credits" to credits))
    }
}

tasks.jar.configure {
    archiveFileName = "$customArchiveBaseName-$modVersionInFileName-dev.jar"
}

tasks.reobfJar.configure {
    archiveFileName = "$customArchiveBaseName-$modVersionInFileName.jar"
    finalizedBy("removeOutput")
}

tasks.sourcesJar.configure {
    enabled = false
    archiveFileName = "$customArchiveBaseName-$modVersionInFileName-sources.jar"
}

tasks.register<Delete>("removeOutput") {
    delete(layout.buildDirectory.file("libs/$customArchiveBaseName-$modVersionInFileName-dev.jar"))
}
