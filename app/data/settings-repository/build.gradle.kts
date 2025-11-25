plugins {
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.google.devtools.ksp)
}

android {
    namespace = GradleConfigs.subNamespaces("settings", "repository")
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
    implementation(libs.dagger.hilt.core)
    implementation(libs.dagger.hilt.android)
}
