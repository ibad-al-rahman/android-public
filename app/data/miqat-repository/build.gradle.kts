plugins {
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.google.devtools.ksp)
}

android {
    namespace = GradleConfigs.subNamespaces("miqat", "repository")
    compileSdk = GradleConfigs.COMPILE_SDK
    defaultConfig {
        minSdk = GradleConfigs.MIN_SDK
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    // `api` because miqat types (IslamicEvent, Coordinates, Method, …) appear in the repository's
    // public domain models (e.g. MiqatData.islamicEvents), so consumers need them on their classpath.
    // Exclude JNA here so its plain JAR doesn't collide with the Android AAR we add below.
    api(libs.miqat) {
        exclude(group = "net.java.dev.jna", module = "jna")
    }
    // The JNA Android AAR packages libjnidispatch.so into jniLibs so miqat's native calls resolve;
    // the plain JAR hides the .so inside the archive and crashes with UnsatisfiedLinkError.
    // `api` so any module depending on miqat-repository inherits the AAR (no per-module re-import).
    api(libs.jna) {
        artifact {
            type = "aar"
        }
    }
    // Gson (pulled transitively via the retrofit gson converter) for persisting the config as JSON.
    implementation(libs.retrofit2.converter.gson)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
}
