plugins {
    id("java")
    alias(libs.plugins.shadow)
    alias(libs.plugins.runpaper)
}

group = "it.einjojo.akani"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.aikar.co/content/groups/aikar/")
    maven("https://repo.akani.dev/releases")
    //maven("https://repo.oraxen.com/releases")
    maven("https://repo.codemc.io/repository/maven-releases/")
    //maven("https://repo.xenondevs.xyz/releases")
    maven("https://mvn.lumine.io/repository/maven-public/")
    maven("https://jitpack.io")
    maven("https://libraries.minecraft.net/")
}

dependencies {

    compileOnly(libs.paper)
    compileOnly(libs.caffeine)
    compileOnly(libs.akanicore)
    compileOnly(libs.mojangauthlib)
    compileOnly(libs.hikaricp)
    compileOnly(libs.luckperms)
    implementation(libs.obliviateinvcore)
    implementation(libs.obliviateinvpagination)
    implementation(libs.acf)

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")


}


tasks {

    build {
        dependsOn("shadowJar")
    }

    withType<Jar> {
        enabled = true
    }

    shadowJar {
        archiveBaseName.set("AkaniCrates")
        archiveVersion.set("")
        archiveClassifier.set("")
        relocate("mc.obliviate.inventory", "it.einjojo.akani.crates.shadow.inventory")
        relocate("co.aikar", "it.einjojo.akani.crates.shadow.aikar")


    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.isIncremental = true
        options.compilerArgs.add("-parameters")
    }

    runServer {
        minecraftVersion("1.20.4")
    }

    processResources {
        filesMatching("plugin.yml") {
            expand(
                mapOf(
                    "version" to project.version.toString(),
                    "caffeine" to libs.caffeine.get(),
                    "hikari" to libs.hikaricp.get()
                )
            )
        }
    }
}

tasks.test {
    useJUnitPlatform()
}