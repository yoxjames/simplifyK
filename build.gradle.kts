import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlinx.benchmark)
    alias(libs.plugins.detekt)
    alias(libs.plugins.dokka)
    `maven-publish`
}

group = "dev.yoxjames"
version = libs.versions.current.get()

benchmark {
    targets {
        register("jvmBench")
    }
}

detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    autoCorrect = true

    dependencies {
        detektPlugins(libs.detekt.formatting)
    }
}

// Kotlin DSL
tasks.withType<Detekt> {
    jvmTarget = libs.versions.jvm.get()
}
tasks.withType<DetektCreateBaselineTask> {
    jvmTarget = libs.versions.jvm.get()
}

kotlin {
    explicitApi()
    jvm {
        compilations.create("bench") {
            associateWith(compilations.getByName("main"))
        }
        jvmToolchain(libs.versions.jvm.get().toInt())
        withJava()
    }
    js(IR) {
        browser {
            testTask(
                Action {
                    useKarma {
                        useChromium()
                        useFirefox()
                    }
                }
            )
        }
        nodejs()
    }

    // TODO: I dont own any Apple products so I cannot build Apple artifacts. However it will probably work....
    // Native: https://kotlinlang.org/docs/native-target-support.html
    // Tier 1
    linuxX64()
    //macosX64()
    //macosArm64()
    //iosSimulatorArm64()
    //iosX64()
    // Tier 2
    linuxArm64()
    //watchosSimulatorArm64()
    //watchosX64()
    //watchosArm32()
    //watchosArm64()
    //tvosSimulatorArm64()
    //tvosX64()
    //tvosArm64()
    //iosArm64()
    // Tier 3
    mingwX64()

    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val commonBench by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.kotlinx.benchmark)
            }
        }
        getByName("jvmBench") {
            dependsOn(commonBench)
        }
    }
}

allprojects {
    repositories {
        mavenCentral()
    }
}

tasks.register("detektAll") {
    allprojects {
        this@register.dependsOn(tasks.withType<Detekt>())
    }
}
