plugins {
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.google.devtools.ksp)
}

android {
    namespace = GradleConfigs.subNamespaces("prayertimes", "repository")
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
    ksp(libs.room.compiler)
    ksp(libs.dagger.hilt.compiler)
    implementation(libs.dagger.hilt.core)
    implementation(libs.dagger.hilt.android)
    implementation(libs.retrofit2)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(projects.app.common.fp)
    implementation(projects.app.common.base)
    implementation(projects.app.common.network)
}
