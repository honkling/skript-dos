plugins {
    id("com.gradleup.shadow") version "8.3.2"
    kotlin("jvm") version "2.0.20"
    `maven-publish`
}

group = "me.honkling"
version = "0.1.0"

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "maven-publish")

    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = "me.honkling.skript"
                artifactId = project.name
                version = "${rootProject.version}"

                from(components["java"])
            }
        }
    }
}

allprojects {
    kotlin {
        jvmToolchain(21)
    }
}