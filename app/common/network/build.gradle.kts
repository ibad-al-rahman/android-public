plugins {
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = GradleConfigs.subNamespace("network")
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
    ksp(libs.dagger.hilt.compiler)
    implementation(libs.dagger.hilt.core)
    implementation(libs.dagger.hilt.android)
    implementation(libs.okhttp3)
    implementation(libs.okhttp3.logging.interceptor)
    implementation(libs.retrofit2)
    implementation(libs.retrofit2.converter.gson)
    implementation(projects.app.common.base)
    debugImplementation(libs.chucker)
    releaseImplementation(libs.chucker.noop)
}
