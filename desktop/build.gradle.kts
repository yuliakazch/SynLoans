import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
	kotlin("multiplatform")
	id("org.jetbrains.compose") version "1.0.0-beta5"
}

kotlin {
	jvm {
		withJava()
	}

	sourceSets {
		named("jvmMain") {
			dependencies {
				implementation(compose.desktop.currentOs)
			}
		}
	}
}

compose.desktop {
	application {
		mainClass = "com.yuliakazachok.desktop.MainKt"

		nativeDistributions {
			targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
			packageName = "SyndicatedLoans"
			packageVersion = "1.0.0"
		}
	}
}