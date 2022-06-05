buildscript {
    extra.apply {
        set("compose_version", "1.2.0-beta02")
    }
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.5.31")
        classpath("com.vanniktech:gradle-maven-publish-plugin:0.19.0")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "7.2.0" apply false
    id("com.android.library") version "7.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.6.21" apply false
}

tasks.create<Delete>("clean") {
    delete(rootProject.buildDir)
}
