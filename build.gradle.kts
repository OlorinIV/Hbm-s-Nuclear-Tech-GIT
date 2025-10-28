
plugins {
    id("com.gtnewhorizons.gtnhconvention")
}

val mod_version: String by project
val mod_build_number: String by project
val modBuildNumberSub: String by project

val modVersion = "${mod_version}_X$mod_build_number.$modBuildNumberSub"
val credits: String by project

val customArchiveBaseName: String by project
val modVersionInFileName = "X$mod_build_number.$modBuildNumberSub"

tasks.processResources.configure {

    filesMatching("mcmod.info") {
        expand(mapOf(
            "version" to modVersion,
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
