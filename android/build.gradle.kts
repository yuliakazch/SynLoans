import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
	id("com.android.application")
	kotlin("android")
}

val composeVersion = findProperty("version.compose") as String
val composeCompilerVersion = findProperty("version.compose.compiler") as String

android {
	compileSdk = (findProperty("android.compileSdk") as String).toInt()

	defaultConfig {
		minSdk = (findProperty("android.minSdk") as String).toInt()
		targetSdk = (findProperty("android.targetSdk") as String).toInt()

		applicationId = "com.yuliakazachok.synloans.android"
		versionCode = 1
		versionName = "1.0"
	}

	buildTypes {
		getByName("release") {
			isMinifyEnabled = false
		}
	}

	buildFeatures {
		compose = true
	}

	compileOptions {
		// Flag to enable support for the new language APIs
		isCoreLibraryDesugaringEnabled = true
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
		freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
	}
	composeOptions {
		kotlinCompilerExtensionVersion = composeCompilerVersion
	}
}

dependencies {
	implementation(project(":shared"))
	//desugar utils
	coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")
	//Compose
	implementation("androidx.compose.ui:ui:$composeVersion")
	implementation("androidx.compose.ui:ui-tooling:$composeVersion")
	implementation("androidx.compose.foundation:foundation:$composeVersion")
	implementation("androidx.compose.material:material:$composeVersion")
	//Compose Utils
	implementation("io.coil-kt:coil-compose:1.4.0")
	implementation("androidx.activity:activity-compose:1.4.0")
	implementation("androidx.navigation:navigation-compose:2.5.0-alpha01")
	implementation("com.google.accompanist:accompanist-insets:0.20.0")
	implementation("com.google.accompanist:accompanist-swiperefresh:0.20.0")
	implementation("com.google.accompanist:accompanist-pager:0.20.0")
	//Coroutines
	val coroutinesVersion = findProperty("version.kotlinx.coroutines")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
	//DI
	val koinVersion = findProperty("version.koin")
	implementation("io.insert-koin:koin-core:$koinVersion")
	implementation("io.insert-koin:koin-android:$koinVersion")
	implementation("io.insert-koin:koin-androidx-compose:$koinVersion")
}