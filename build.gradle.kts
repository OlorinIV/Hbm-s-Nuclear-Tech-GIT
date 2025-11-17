
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

tasks.register<Delete>("delDevJar") { 
    delete(layout.buildDirectory.file("libs/$customArchiveBaseName-$modVersionInFileName-dev.jar"))
    delete(layout.buildDirectory.file("libs/$customArchiveBaseName-$modVersionInFileName-sources.jar"))
}
