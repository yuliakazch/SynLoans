import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
	kotlin("multiplatform")
	id("org.jetbrains.compose") version "1.0.1"
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

				implementation("io.github.alexgladkov:odyssey-core:0.2.0")
				implementation("io.github.alexgladkov:odyssey-compose:0.2.0")
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