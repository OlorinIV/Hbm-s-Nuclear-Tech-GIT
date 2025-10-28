
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
    enabled = false
    archiveFileName = "$customArchiveBaseName-$modVersionInFileName.jar"
    //finalizedBy("removeOutput")
}

tasks.sourcesJar.configure {
    enabled = false
    archiveFileName = "$customArchiveBaseName-$modVersionInFileName-sources.jar"
}

//If you do not like the dev jar I think I could remove this... maybe
//tasks.register<Delete>("removeOutput") {
//    delete(layout.buildDirectory.file("libs/$customArchiveBaseName-$modVersionInFileName-dev.jar"))
//}
