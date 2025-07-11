plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.compose)
}

android {
    namespace = GradleConfigs.subNamespace("mvi")
    compileSdk = GradleConfigs.COMPILE_SDK

    defaultConfig {
        minSdk = GradleConfigs.MIN_SDK
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    ksp(libs.dagger.hilt.compiler)
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
