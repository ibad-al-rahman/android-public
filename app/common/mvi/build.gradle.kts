plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.kapt)
    alias(libs.plugins.jetbrains.kotlin.android)
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
}

android {
    namespace = GradleConfigs.subNamespace("mvi")
    compileSdk = GradleConfigs.compileSdk

    defaultConfig {
        minSdk = GradleConfigs.minSdk
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    kapt(libs.dagger.hilt.compiler)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
    implementation(libs.androidx.ui.tooling)
    implementation(libs.dagger.hilt.core)
    implementation(libs.dagger.hilt.android)
    implementation(projects.app.common.base)
    implementation(platform(libs.androidx.compose.bom))
}
