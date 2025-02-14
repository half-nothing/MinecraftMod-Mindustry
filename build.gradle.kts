import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.text.SimpleDateFormat
import java.util.*

plugins {
    idea
    kotlin("jvm") version "2.1.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.7"
    id("com.github.jakemarsden.git-hooks") version "0.0.2"
    id("net.minecraftforge.gradle") version "[6.0,6.2)"
    id("org.parchmentmc.librarian.forgegradle") version "1.+"
    id("org.spongepowered.mixin") version "0.7.+"
}

repositories {
    maven("https://maven.aliyun.com/repository/public/")
    maven("https://maven.aliyun.com/repository/gradle-plugin/")
    maven("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/") {
        name = "GeckoLib"
    }
    maven("https://dvs1.progwml6.com/files/maven/") {
        name = "Progwml6's maven"
    }
    maven("https://maven.blamejared.com/") {
        name = "Jared's maven"
    }
    maven("https://modmaven.dev") {
        name = "ModMaven"
    }
    maven("https://maven.zerono.it/") {
        name = "zerono"
    }
    maven("https://cursemaven.com") {
        content {
            includeGroup("curse.maven")
        }
    }
    mavenLocal()
    mavenCentral()
}

jarJar.enable()

val minecraft_version: String by project
val forge_version: String by project
val mapping_channel: String by project
val mapping_version: String by project
val jei_version: String by project
val geckolib_version: String by project
val create_id: String by project
val betterF3_id: String by project
val clothConfigAPI_id: String by project
val jade_id: String by project
val justEnoughCharacters_id: String by project
val kiwi_fileId: String by project
val minecraft_version_range: String by project
val forge_version_range: String by project
val loader_version_range: String by project
val mod_id: String by project
val mod_name: String by project
val mod_license: String by project
val mod_version: String by project
val mod_authors: String by project
val mod_description: String by project
val mod_group_id: String by project

dependencies {
    minecraft("net.minecraftforge:forge:$minecraft_version-$forge_version")
    compileOnly(fg.deobf("mezz.jei:jei-$minecraft_version-common-api:$jei_version"))
    compileOnly(fg.deobf("mezz.jei:jei-$minecraft_version-forge-api:$jei_version"))
    runtimeOnly(fg.deobf("mezz.jei:jei-${minecraft_version}-forge:${jei_version}"))
    implementation(fg.deobf("software.bernie.geckolib:geckolib-forge-${minecraft_version}:${geckolib_version}"))
    implementation(fg.deobf("curse.maven:create-328085:${create_id}"))
    implementation(fg.deobf("curse.maven:BetterF3-401648:${betterF3_id}"))
    implementation(fg.deobf("curse.maven:ClothConfigAPI-348521:${clothConfigAPI_id}"))
    implementation(fg.deobf("curse.maven:jade-324717:${jade_id}"))
    implementation(fg.deobf("curse.maven:just-enough-characters-250702:${justEnoughCharacters_id}"))
    implementation(fg.deobf("curse.maven:kiwi-303657:${kiwi_fileId}"))
    implementation("org.apache.commons:commons-io:1.3.2")
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
}

minecraft {
    mappings(mapping_channel, mapping_version)
    enableIdeaPrepareRuns.set(true)
    copyIdeResources.set(true)
    accessTransformers(file("src/main/resources/META-INF/accesstransformer.cfg"))
    runs {
        configureEach {
            workingDirectory(project.file("run"))

            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")

            mods {
                create(mod_id) {
                    source(sourceSets.main.get())
                }
            }
        }

        create("client") {
            property("forge.enabledGameTestNamespaces", mod_id)
            args("--mixin.config=mixins.mindustry.json")
        }

        create("server") {
            property("forge.enabledGameTestNamespaces", mod_id)
            args("--mixin.config=mixins.mindustry.json", "--nogui")
        }

        create("gameTestServer") {
            property("forge.enabledGameTestNamespaces", mod_id)
        }

        create("data") {
            args(
                "--mod",
                mod_id,
                "--all",
                "--output",
                file("src/generated/resources/"),
                "--existing",
                file("src/main/resources/")
            )
        }
    }
}

val targetJavaVersion = 17

sourceSets.main {
    resources {
        srcDirs("src/generated/resources")
    }
}

group = mod_group_id
version = "$mod_version${getVersionMetadata()}"

base {
    archivesName.set(mod_id)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
}

kotlin {
    jvmToolchain(targetJavaVersion)
}

mixin {
    add(sourceSets.main.get(), "mixins.mindustry.json")
}

tasks {
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release.set(targetJavaVersion)
    }

    withType<KotlinCompile>().configureEach {
        compilerOptions.jvmTarget.set(JvmTarget.fromTarget(targetJavaVersion.toString()))
    }

    processResources {
        val resourceTargets = mutableListOf("META-INF/mods.toml", "pack.mcmeta")
        val replaceProperties = mutableMapOf(
            "minecraft_version" to minecraft_version,
            "minecraft_version_range" to minecraft_version_range,
            "forge_version" to forge_version,
            "forge_version_range" to forge_version_range,
            "loader_version_range" to loader_version_range,
            "mod_id" to mod_id,
            "mod_name" to mod_name,
            "mod_license" to mod_license,
            "mod_version" to mod_version,
            "mod_authors" to mod_authors,
            "mod_description" to mod_description
        )

        inputs.properties(replaceProperties)

        filesMatching(resourceTargets) {
            expand(replaceProperties)
        }
    }

    jar {
        finalizedBy("reobfJar")
        manifest {
            attributes(
                "Build-By" to System.getProperty("user.name"),
                "Build-TimeStamp" to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(Date()),
                "Build-Version" to version,
                "Version-Type" to getVersionMetadata(),
                "Created-By" to "Gradle ${gradle.gradleVersion}",
                "Build-Jdk" to "${System.getProperty("java.version")} " +
                        "(${System.getProperty("java.vendor")} ${System.getProperty("java.vm.version")})",
                "Build-OS" to "${System.getProperty("os.name")} " +
                        "${System.getProperty("os.arch")} ${System.getProperty("os.version")}",
                "Specification-Title" to mod_id,
                "Specification-Vendor" to mod_authors,
                "Specification-Version" to "1",
                "Implementation-Title" to project.name,
                "Implementation-Vendor" to mod_authors,
                "MixinConfigs" to "mixins.mindustry.json",
                "FMLAT" to "accesstransformer.cfg"
            )
        }
    }
}

// code style check for kotlin
detekt {
    buildUponDefaultConfig = true
    autoCorrect = true
    config.setFrom(rootProject.files("detekt.yml"))
}

// git hooks for automatically checking code style
gitHooks {
    setHooks(
        mapOf("pre-commit" to "detekt")
    )
}

fun getVersionMetadata(): String {
    val buildId = System.getenv("GITHUB_RUN_NUMBER")
    val workflow = System.getenv("GITHUB_WORKFLOW")
    val release = System.getenv("RELEASE")

    if (workflow == "Release" || !release.isNullOrBlank()) {
        return ""
    }

    // CI builds only
    if (!buildId.isNullOrBlank()) {
        return "+build.$buildId"
    }

    // No tracking information could be found about the build
    return "+nightly.${(System.currentTimeMillis() % 1e6).toInt().toString().padStart(6, '0')}"
}
