
plugins {
    id("com.gtnewhorizons.gtnhconvention")
}

val mod_version: String by project
val mod_build_number: String by project
val mod_build_number_sub: String by project

val modVersion = "${mod_version}_X$mod_build_number.$mod_build_number_sub"
val minecraftVersion: String by project
val modId: String by project
val modName: String by project
val credits: String by project

val customArchiveBaseName: String by project
val modVersionInFileName = "X$mod_build_number.$mod_build_number_sub"

tasks.processResources.configure {
    filesMatching("mcmod.info") {
        expand(mapOf(
            "modVersion" to modVersion,
            "minecraftVersion" to minecraftVersion,
            "modId" to modId,
            "modName" to modName,
            "credits" to credits)
        )
    }
}

tasks.jar.configure {
    archiveFileName = "$customArchiveBaseName-$modVersionInFileName-dev.jar"
}

tasks.reobfJar.configure {
    archiveFileName = "$customArchiveBaseName-$modVersionInFileName.jar"
    //finalizedBy("removeOutput")
}

tasks.sourcesJar.configure {
    enabled = false
    archiveFileName = "$customArchiveBaseName-$modVersionInFileName-sources.jar"
}

//If you do not like the dev jar I think I could delete this elsewhere
//tasks.register<Delete>("removeOutput") {
//    delete(layout.buildDirectory.file("libs/$customArchiveBaseName-$modVersionInFileName-dev.jar"))
//}
