buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${findProperty("version.kotlin")}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${findProperty("version.kotlin")}")
        classpath("com.android.tools.build:gradle:${findProperty("version.androidGradlePlugin")}")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}