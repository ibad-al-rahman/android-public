---
name: new-gradle-module
description: Add a new Gradle library module (screen, data, common, or widget) to this Android app, wired to settings.gradle.kts, GradleConfigs, the version catalog, and type-safe project accessors. Use when creating a new feature module or extracting code into its own module.
---

# Add a new Gradle module

Create a new Android **library** module matching the repo's conventions. **Copy the closest existing
sibling rather than writing from scratch:**
- A **screen** → copy `app/screens/settings/build.gradle.kts`.
- A **data/repository** module → copy `app/data/settings-repository/build.gradle.kts`.
- A lean **common** utility → copy `app/common/mvi/build.gradle.kts`.

Key facts (verified against the repo):
- Base namespace + SDK levels come from `buildSrc/src/main/kotlin/GradleConfig.kt`:
  `GradleConfigs.subNamespace("x")`, `GradleConfigs.COMPILE_SDK`, `GradleConfigs.MIN_SDK`.
  (No `targetSdk` is set in modules — don't add one.)
- `TYPESAFE_PROJECT_ACCESSORS` is enabled → depend on modules via `projects.app.common.mvi`, never
  `project(":app:common:mvi")`.
- All versions/plugins are aliases from `gradle/libs.versions.toml` (`libs.*`). Never write a literal
  version in a module build file — add it to the catalog first.
- JDK 21 (`JavaVersion.VERSION_21`); screens that use Compose also set `kotlinOptions { jvmTarget = "21" }`.
- The app is a `com.android.application`; **every other module is a `com.android.library`.**

## Steps

### 1. Register in `settings.gradle.kts`
Add an `include(...)` next to its siblings, keeping the existing grouping/blank-line layout:
```kotlin
include(":app:screens:my-feature")
```
The type-safe accessor is derived automatically: `:app:screens:my-feature` → `projects.app.screens.myFeature`
(kebab-case segments become camelCase).

### 2. Create `app/<group>/<my-module>/build.gradle.kts`

**Compose screen module** (mirrors `app/screens/settings/build.gradle.kts`):
```kotlin
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.jetbrains.kotlin.compose)
}

android {
    namespace = GradleConfigs.subNamespace("myfeature")
    compileSdk = GradleConfigs.COMPILE_SDK

    defaultConfig {
        minSdk = GradleConfigs.MIN_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
}

dependencies {
    ksp(libs.dagger.hilt.compiler)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.material.icons.extended)
    implementation(libs.dagger.hilt.android)

    // A screen module typically depends on these:
    implementation(projects.app.common.fp)
    implementation(projects.app.common.mvi)
    implementation(projects.app.common.base)
    implementation(projects.app.common.resources)
    // plus the data modules it needs, e.g.:
    // implementation(projects.app.data.settingsRepository)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
```

For a **non-Compose module** (utility / data), drop the `compose` plugin and Compose dependencies,
and omit `kotlinOptions`/`buildTypes` if the sibling you're copying does (see `app/common/mvi`).
Depend on `projects.app.common.base` and whatever the module actually needs.

### 3. Add source root + supporting files
- Source dir: `src/main/java/org/ibadalrahman/<namespace-path>/`.
- If the build file references them, add empty `consumer-rules.pro` / `proguard-rules.pro`
  (copy the sibling's — most modules just inherit them).
- No `AndroidManifest.xml` is needed for a code-only library unless it declares components.

### 4. Wire consumers
Add `implementation(projects...)` for the new module in whichever module(s) use it (e.g. `:app`, or a
screen). Keep the `common:base ← common:mvi ← screens` layering — don't create cycles.

## Verify
- `./gradlew :app:<group>:<my-module>:help` resolves the project.
- `./gradlew build` compiles the whole tree.
- Optionally regenerate the module graph in `ARCHITECTURE.md` (the `module-graph` plugin is
  configured — `libs.plugins.module.graph`).
