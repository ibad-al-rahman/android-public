import java.text.SimpleDateFormat
import java.util.*

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.dagger.hilt.android)
    alias(libs.plugins.jetbrains.kotlin.compose)
    alias(libs.plugins.google.gsm.google.services)
}

val keyStoreFile = rootProject.file("publicSector.keystore")
val shouldSign = keyStoreFile.exists()
val props = Properties()
val propsFile = rootProject.file("keys.properties")

if (propsFile.exists()) {
    propsFile.inputStream().use { props.load(it) }
}

android {
    val appId = GradleConfigs.subNamespace("publicsector")

    namespace = appId
    compileSdk = GradleConfigs.compileSdk

    defaultConfig {
        applicationId = appId
        minSdk = GradleConfigs.minSdk
        targetSdk = GradleConfigs.compileSdk
        versionCode = (System.currentTimeMillis() / 1000).toInt()
        versionName = SimpleDateFormat("yy.MM.dd", Locale.US).format(Date())

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    val releaseSigningConfigName = "release"

    signingConfigs {
        create(releaseSigningConfigName) {
            if (shouldSign) {
                storeFile = keyStoreFile
                storePassword = props.getProperty("keystore.password")
                keyAlias = props.getProperty("keystore.alias.name")
                keyPassword = props.getProperty("keystore.alias.password")
            }
        }

        // We can create a debug signing config when we want to release beta debug builds
    }

    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            if (shouldSign) {
                signingConfig = signingConfigs.getByName(releaseSigningConfigName)
            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            versionNameSuffix = "-debug"
            isDebuggable = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/*"
            excludes += "/*.properties"
            excludes += "META-INF/INDEX.LIST"
            excludes += "META-INF/io.netty.versions.properties"
        }
    }
}

dependencies {
    ksp(libs.dagger.hilt.compiler)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.material.icons.extended)
    implementation(libs.dagger.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.appcompat)

    implementation(projects.app.common.base)
    implementation(projects.app.common.mvi)
    implementation(projects.app.common.resources)
    implementation(projects.app.screens.prayerTimes)
    implementation(projects.app.screens.settings)
    implementation(projects.app.widgets.prayerTimes)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
