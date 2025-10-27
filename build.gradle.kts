
plugins {
    id("com.gtnewhorizons.gtnhconvention")
}

val mod_build_number: String by project
val mod_build_number_sub: String by project
val credits: String by project

val version = "X$mod_build_number.$mod_build_number_sub"

tasks.processResources.configure {
    
    filesMatching("mcmod.info") {
        expand(mapOf("version" to version, "credits" to credits))
    }
}

tasks.jar.configure {
    archiveFileName = "NTMC_$version.jar"
}