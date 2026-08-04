---
name: mvi-feature
description: Scaffold a complete MVI feature (Intention, Action, Result, Reducer, ViewModel, Interactor, ScreenState, ViewAction, Screen) in this Android app, following the CalculationMethod reference pattern. Use when adding a new screen or feature to a screens/* module.
---

# Scaffold an MVI feature

Generate the full set of MVI files for a new feature in a `:app:screens:*` module, matching the
established pattern. **The `calculationmethod/` feature is the canonical reference — read those files
before generating, since they are the living source of truth and may have evolved past this skill.**

Reference files (in `app/screens/settings/src/main/java/org/ibadalrahman/settings/calculationmethod/`):
- `domain/CalculationMethodInteractor.kt`, `domain/entity/CalculationMethodAction.kt`, `domain/entity/CalculationMethodResult.kt`
- `presenter/CalculationMethodViewModel.kt`, `presenter/CalculationMethodReducer.kt`
- `presenter/entity/CalculationMethodIntention.kt`, `CalculationMethodScreenState.kt`, `CalculationMethodViewAction.kt`
- `view/CalculationMethodSelectionScreen.kt`
- Test: `app/screens/settings/src/test/java/org/ibadalrahman/settings/calculationmethod/CalculationMethodReducerTest.kt`

Base framework: `app/common/mvi/` (`BaseViewModel`, `BaseInteractor`, `BaseScreen`, `MviBoundary`).

## Inputs to establish first

1. **Feature name** in PascalCase, e.g. `Foo` → classes `FooViewModel`, `FooReducer`, etc.
2. **Target module + package**, e.g. module `:app:screens:settings`, package
   `org.ibadalrahman.settings.foo`. Confirm the module already depends on `common:{fp,mvi,base,resources}`
   (add via `/new-gradle-module` conventions if not).
3. **What the feature does** — which Intentions the UI emits, what the Interactor persists/reads
   (usually via an injected repository), and any one-shot ViewActions (navigation).

## Files to create

Under `.../<package-path>/foo/` (replace `Foo`/`foo` and the package):

### 1. `domain/entity/FooAction.kt`
```kotlin
package org.ibadalrahman.settings.foo.domain.entity

sealed interface FooAction {
    data object Load : FooAction
    // data class SetValue(val value: String) : FooAction
}
```

### 2. `domain/entity/FooResult.kt`
```kotlin
package org.ibadalrahman.settings.foo.domain.entity

sealed interface FooResult {
    data class Loaded(val value: String) : FooResult
}
```

### 3. `domain/FooInteractor.kt`
```kotlin
package org.ibadalrahman.settings.foo.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.ibadalrahman.mvi.BaseInteractor
import org.ibadalrahman.settings.foo.domain.entity.FooAction
import org.ibadalrahman.settings.foo.domain.entity.FooResult
import javax.inject.Inject

class FooInteractor @Inject constructor(
    // private val repository: SomeRepository,
) : BaseInteractor<FooAction, FooResult> {

    override suspend fun resultFrom(action: FooAction): Flow<FooResult> {
        when (action) {
            FooAction.Load -> Unit
            // is FooAction.SetValue -> repository.set(action.value)
        }
        return flowOf(FooResult.Loaded(value = /* repository.get() */ ""))
    }
}
```

### 4. `presenter/entity/FooIntention.kt`
```kotlin
package org.ibadalrahman.settings.foo.presenter.entity

sealed interface FooIntention {
    data object Load : FooIntention
    // data class SetValue(val value: String) : FooIntention
}
```

### 5. `presenter/entity/FooScreenState.kt`
```kotlin
package org.ibadalrahman.settings.foo.presenter.entity

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
@Immutable
data class FooScreenState(
    val value: String? = null,
) {
    companion object {
        val Empty = FooScreenState()
    }
}
```

### 6. `presenter/entity/FooViewAction.kt`
```kotlin
package org.ibadalrahman.settings.foo.presenter.entity

sealed interface FooViewAction {
    // data object NavigateBack : FooViewAction
}
```
If the feature has no side effects, keep this interface with a comment and omit `viewActionFrom()`
from the ViewModel (its default returns `null`).

### 7. `presenter/FooReducer.kt`
```kotlin
package org.ibadalrahman.settings.foo.presenter

import org.ibadalrahman.settings.foo.domain.entity.FooResult
import org.ibadalrahman.settings.foo.presenter.entity.FooScreenState

object FooReducer {
    fun reduce(
        prevState: FooScreenState,
        result: FooResult,
    ): FooScreenState = when (result) {
        is FooResult.Loaded -> prevState.copy(value = result.value)
    }
}
```

### 8. `presenter/FooViewModel.kt`
```kotlin
package org.ibadalrahman.settings.foo.presenter

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import org.ibadalrahman.base.CoroutineDispatchers
import org.ibadalrahman.mvi.BaseViewModel
import org.ibadalrahman.mvi.MviBoundary
import org.ibadalrahman.settings.foo.domain.FooInteractor
import org.ibadalrahman.settings.foo.domain.entity.FooAction
import org.ibadalrahman.settings.foo.domain.entity.FooResult
import org.ibadalrahman.settings.foo.presenter.entity.FooIntention
import org.ibadalrahman.settings.foo.presenter.entity.FooScreenState
import org.ibadalrahman.settings.foo.presenter.entity.FooViewAction
import javax.inject.Inject

@HiltViewModel
class FooViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    coroutineDispatchers: CoroutineDispatchers,
    interactor: FooInteractor,
) : BaseViewModel<
        FooScreenState,
        FooIntention,
        FooViewAction,
        FooAction,
        FooResult
        >(
    savedStateHandle = savedStateHandle,
    coroutineDispatchers = coroutineDispatchers,
    initialState = FooScreenState.Empty,
    interactor = interactor,
) {
    override fun router(
        intention: FooIntention
    ): MviBoundary<FooViewAction, FooAction, FooResult> =
        when (intention) {
            FooIntention.Load -> action(FooAction.Load)
            // is FooIntention.SetValue -> action(FooAction.SetValue(intention.value))
        }

    override fun reduce(result: FooResult) {
        updateState { FooReducer.reduce(prevState = this, result = result) }
    }

    // Only override when the feature has side effects:
    // override fun viewActionFrom(result: FooResult): FooViewAction? = when (result) { ... }
}
```

### 9. `view/FooScreen.kt`
```kotlin
package org.ibadalrahman.settings.foo.view

import androidx.compose.runtime.Composable
import org.ibadalrahman.mvi.BaseScreen
import org.ibadalrahman.settings.foo.presenter.FooViewModel
import org.ibadalrahman.settings.foo.presenter.entity.FooIntention

@Composable
fun FooScreen(
    viewModel: FooViewModel,
) {
    BaseScreen(viewModel = viewModel, viewActionProcessor = { /* handle FooViewAction */ }) { state, intentionProcessor ->
        // Emit FooIntention.Load on start (see calculationmethod/view/LoadOnce.kt for the
        // ObserveLifecycleEvents pattern), then render `state` and call
        // intentionProcessor(FooIntention...) on user interaction.
    }
}
```

### 10. Reducer test — `src/test/.../foo/FooReducerTest.kt`
```kotlin
package org.ibadalrahman.settings.foo

import org.ibadalrahman.settings.foo.domain.entity.FooResult
import org.ibadalrahman.settings.foo.presenter.FooReducer
import org.ibadalrahman.settings.foo.presenter.entity.FooScreenState
import org.junit.Assert.assertEquals
import org.junit.Test

class FooReducerTest {

    @Test
    fun `Loaded copies value into state`() {
        val state = FooReducer.reduce(
            prevState = FooScreenState.Empty,
            result = FooResult.Loaded(value = "hi"),
        )

        assertEquals("hi", state.value)
    }
}
```

## After generating

- Wire the screen into navigation where the module composes its screens.
- If the interactor needs a repository, inject an existing one (`SettingsRepository`,
  `MiqatRepository`) via `@Inject constructor` — no manual Hilt module needed for the interactor.
- Add any user-facing strings to **both** `values/strings.xml` and `values-ar/strings.xml`.
- Verify: `./gradlew :app:screens:<module>:test` (and `./gradlew build` for the whole tree).
