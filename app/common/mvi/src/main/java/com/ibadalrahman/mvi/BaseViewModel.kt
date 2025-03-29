package com.ibadalrahman.mvi

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import org.ibadalrahman.base.CoroutineDispatchers

abstract class BaseViewModel<UiState: Any, Intention: Any, ViewAction: Any, Action: Any, Result: Any>(
    open val savedStateHandle: SavedStateHandle,
    open val coroutineDispatchers: CoroutineDispatchers,
    val initialState: UiState,
    private val interactor: BaseInteractor<Action, Result>
): ViewModel() {
    @PublishedApi
    internal val mState: MutableStateFlow<UiState> = MutableStateFlow(initialState)
    val state: StateFlow<UiState> = mState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = initialState
    )

    private val intentions = MutableSharedFlow<Intention>()
    private val _viewActions = Channel<ViewAction>(Channel.BUFFERED)
    val viewActions = _viewActions.receiveAsFlow()

    private fun collectIntentions() = viewModelScope.launch {
        intentions.collect { intention ->
            when (val either = router(intention)) {
                is MviBoundary.Action -> {
                    viewModelScope.launch(coroutineDispatchers.computation) {
                        interactor.resultFrom(either.value).collect { result -> resultHandler(result) }
                    }
                }
                is MviBoundary.ViewAction -> submitViewAction(either.value)
                is MviBoundary.Result -> resultHandler(either.value)
            }
        }
    }

    private fun resultHandler(result: Result) {
        viewActionFrom(result)?.let { submitViewAction(it) }
        reduce(result)
    }

    val submitIntention: (intention: Intention) -> Unit = {
        viewModelScope.launch { intentions.emit(it) }
    }

    val submitViewAction: (viewAction: ViewAction) -> Unit = {
        viewModelScope.launch { _viewActions.send(it) }
    }

    inline fun updateState(crossinline reduce: UiState.() -> UiState): UiState {
        return mState.updateAndGet(reduce)
    }

    inline fun withState(doWithState: UiState.() -> Unit) {
        state.value.doWithState()
    }

    inline fun <R> fromState(doWithState: UiState.() -> R): R {
        return state.value.doWithState()
    }

    fun <T> getFromSavedStateAsFlow(key: String) = savedStateHandle.getStateFlow<T?>(key, null)

    fun <T> saveToSavedState(key: String, value: T?) = savedStateHandle.set(key, value)

    private fun <T> LiveData<T?>.asFlow(): Flow<T?> = callbackFlow {
        val observer = Observer<T?> { value -> trySend(value) }
        observeForever(observer)
        awaitClose {
            removeObserver(observer)
        }
    }

    init {
        collectIntentions()
    }

    abstract fun router(intention: Intention): MviBoundary<ViewAction, Action, Result>
    abstract fun reduce(result: Result)

    open fun viewActionFrom(result: Result): ViewAction? = null

    protected fun action(action: Action) = MviBoundary.Action(action)
    protected fun viewAction(viewAction: ViewAction) = MviBoundary.ViewAction(viewAction)
    protected fun result(result: Result) = MviBoundary.Result(result)
}

sealed class MviBoundary<out ViewAction: Any, out Action: Any, out Result: Any> {
    data class ViewAction<ViewAction: Any>(val value: ViewAction): MviBoundary<ViewAction, Nothing, Nothing>()
    data class Action<Action: Any>(val value: Action): MviBoundary<Nothing, Action, Nothing>()
    data class Result<Result: Any>(val value: Result): MviBoundary<Nothing, Nothing, Result>()
}
