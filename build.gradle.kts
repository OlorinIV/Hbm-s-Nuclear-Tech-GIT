
plugins {
    id("com.gtnewhorizons.gtnhconvention")
}

val modMainVersion: String by project
val modBuildNumber: String by project
val modBuildNumberSub: String by project

val modVersion = modMainVersion + "_X" + modBuildNumber + "." + modBuildNumberSub
val minecraftVersion: String by project
val modId: String by project
val modName: String by project
val credits: String by project

val customArchiveBaseName: String by project

//val version = "X$mod_build_number.$mod_build_number_sub"

tasks.processResources.configure {

    filesMatching("mcmod.info") {
        expand(mapOf(
            "modVersion" to modVersion,
            "minecraftVersion" to minecraftVersion,
            "modId" to modId,
            "modName" to modName,
            "credits" to credits))
    }
}

tasks.jar.configure {
    archiveFileName = "$customArchiveBaseName-$modVersion-dev.jar"
}

tasks.reobfJar.configure {
    archiveFileName = "$customArchiveBaseName-$modVersion.jar"
}

tasks.sourcesJar.configure {
    archiveFileName = "$customArchiveBaseName-$modVersion-sources.jar"
}

//tasks.withType<JavaCompile> {
//    options.encoding = "UTF-8"
//}
//tasks.withType<JavaExec> {
//    systemProperty("file.encoding", "utf-8")
//}
