plugins {
    alias(libs.plugins.jetbrains.kotlin.kapt)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.android.library)
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
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
    kapt(libs.dagger.hilt.compiler)
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
