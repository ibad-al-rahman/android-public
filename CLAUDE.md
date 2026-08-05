# CLAUDE.md

Guidance for Claude Code when working in this repository.

## Project

**Ibad Al-Rahman** (عباد الرحمن) — a bilingual (English / Arabic, RTL) Islamic prayer-times Android
app. Base package: `org.ibadalrahman`. Gradle root project name: `PublicSector`.

## Build & run

- **Build (debug APK):** `./gradlew assembleDebug`
- **Full build:** `./gradlew build`
- **Release bundle:** `./gradlew bundleRelease`
- **JDK 21** is required (source/target `VERSION_21`, `jvmTarget = "21"`).

All dependency versions live in the version catalog at `gradle/libs.versions.toml`. **Never hardcode
a version in a module `build.gradle.kts`** — add it to the catalog and reference it via a `libs.*`
alias. SDK levels and the base namespace come from `buildSrc/src/main/kotlin/GradleConfig.kt`
(`GradleConfigs.COMPILE_SDK = 35`, `MIN_SDK = 26`). Key versions: Gradle 8.13, AGP 8.11.1, Kotlin
2.0.21, Compose BOM 2025.06.01, Hilt 2.51.1. `TYPESAFE_PROJECT_ACCESSORS` is enabled — depend on
modules via `projects.app.common.mvi`, not `project(":app:common:mvi")`.

## Test

- **Unit tests:** `./gradlew test`
- **Instrumented tests:** `./gradlew androidTest`

Tests use **JUnit 4** with backtick BDD-style names and `org.junit.Assert` assertions. No MockK,
Turbine, or Kotest. Unit tests go in `src/test/`, instrumented tests in `src/androidTest/`.
**Reducers are pure functions and are the primary unit-tested surface** — see
`app/screens/settings/src/test/.../CalculationMethodReducerTest.kt` for the canonical example:

```kotlin
@Test
fun `Loaded copies both method and preview into state`() {
    val state = CalculationMethodReducer.reduce(
        prevState = CalculationMethodScreenState.Empty,
        result = CalculationMethodResult.Loaded(method = method, preview = null),
    )
    assertEquals(method, state.method)
}
```

**CI runs no automated PR checks** (the GitHub Actions workflows are manual-dispatch deploys only).
Run `./gradlew build` — or at minimum `./gradlew test` — locally before pushing.

## Architecture — MVI

Unidirectional data flow. The base framework is in `app/common/mvi/` (`BaseViewModel`,
`BaseInteractor`, `BaseScreen`, `MviBoundary`). See `ARCHITECTURE.md` for the flow diagram.

```
UI --intention--> router() --action--> Interactor.resultFrom() --Flow<Result>-->
    ├─ Reducer.reduce(prevState, result) --> new State  (UI re-renders)
    └─ viewActionFrom(result) --> ViewAction            (navigation / side effects)
```

`router()` maps an **Intention** to a `MviBoundary` via `action(...)`, `viewAction(...)`, or
`result(...)`. `reduce()` delegates to a pure `{Feature}Reducer` object. `viewActionFrom()`
(optional) emits one-shot side effects.

### Per-feature file layout

Every feature is scaffolded identically. `calculationmethod/` is the reference. For a feature `Foo`:

```
foo/
├── domain/
│   ├── FooInteractor.kt              # @Inject constructor, : BaseInteractor<FooAction, FooResult>
│   └── entity/
│       ├── FooAction.kt              # sealed interface + data object / data class
│       └── FooResult.kt              # sealed interface + data object / data class
├── presenter/
│   ├── FooViewModel.kt               # @HiltViewModel; router(), reduce(), optional viewActionFrom()
│   ├── FooReducer.kt                 # object; pure reduce(prevState, result): FooScreenState
│   └── entity/
│       ├── FooIntention.kt           # sealed interface
│       ├── FooScreenState.kt         # @Stable @Immutable data class + companion { val Empty }
│       └── FooViewAction.kt          # sealed interface
└── view/
    └── FooScreen.kt                  # composable wrapping BaseScreen(...); nested *Row.kt components
```

To scaffold a new feature, use the **`/mvi-feature`** skill.

## Module graph

```
:app
:app:common:{fp, base, mvi, network, resources}
:app:data:{settings-repository, miqat-repository}
:app:screens:{prayer-times, settings}
:app:widgets:prayer-times
```

A screen module typically depends on `common:{fp, mvi, base, resources}` plus the `data:*` modules
it needs. `common:mvi` depends on `common:base`; `common:network` depends on `common:base`. To add a
new module, use the **`/new-gradle-module`** skill.

> **Note:** `ARCHITECTURE.md`'s module graph is slightly stale — it names a single
> `prayer-times-repository`, but the actual data modules are `settings-repository` and
> `miqat-repository`. Trust `settings.gradle.kts`.

## Conventions

- **DI:** Hilt. ViewModels are `@HiltViewModel` with `@Inject constructor`; interactors use
  `@Inject constructor`. Data-layer bindings use
  `@Module @InstallIn(SingletonComponent::class) abstract class {X}Binds { @Binds ... }`.
- **Entities:** model Actions / Results / Intentions / ViewActions as `sealed interface`s with
  `data object` (no args) or `data class` (with args) cases.
- **State:** `@Stable @Immutable data class` with a `companion object { val Empty }` default;
  computed view-slices as `val ... get()`.
- **UI:** Jetpack Compose + Material 3 only — no XML layouts. Colors/type from `MaterialTheme`.
  Widgets use Glance.
- **Code style:** Kotlin `official` style (`kotlin.code.style=official`). Only `.editorconfig` is
  enforced (final newline, trim trailing whitespace) — no detekt/ktlint/spotless.
- **Localization:** any user-facing string must be added to **both**
  `app/common/resources/src/main/res/values/strings.xml` (English) **and** `values-ar/strings.xml`
  (Arabic). The app supports RTL.

## Git

- **Conventional Commits:** `feat:`, `fix:`, `chore:`, `docs:`.
- **Feature branches:** `feat/<slug>`, based off `master`.
- **Never commit directly to `master`.**
- **Do not co-author commits.**

## Skills

- **`/mvi-feature`** — scaffold a complete MVI feature (all 8 files) following the CalculationMethod pattern.
- **`/new-gradle-module`** — add a new Gradle module wired to the existing conventions.
