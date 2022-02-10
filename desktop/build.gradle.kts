import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("multiplatform")
	id("org.jetbrains.compose") version "1.0.1"
}

tasks.withType<KotlinCompile> {
	kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
}

kotlin {
	jvm {
		withJava()
	}

	sourceSets {
		named("jvmMain") {
			dependencies {
				implementation(compose.desktop.currentOs)
				implementation(project(":shared"))

				//DI
				val koinVersion = findProperty("version.koin")
				implementation("io.insert-koin:koin-core:$koinVersion")

				//Navigation
				val voyagerVersion = findProperty("version.voyager")
				implementation("cafe.adriel.voyager:voyager-navigator:$voyagerVersion")
				implementation("cafe.adriel.voyager:voyager-tab-navigator:$voyagerVersion")
				implementation("cafe.adriel.voyager:voyager-koin:$voyagerVersion")
			}
		}
	}
}

compose.desktop {
	application {
		mainClass = "com.yuliakazachok.synloans.desktop.MainKt"

		nativeDistributions {
			targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
			packageName = "SyndicatedLoans"
			packageVersion = "1.0.0"
		}
	}
}