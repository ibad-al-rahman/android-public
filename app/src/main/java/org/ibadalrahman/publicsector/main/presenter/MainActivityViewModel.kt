package org.ibadalrahman.publicsector.main.presenter

import androidx.lifecycle.SavedStateHandle
import org.ibadalrahman.mvi.BaseViewModel
import org.ibadalrahman.mvi.MviBoundary
import dagger.hilt.android.lifecycle.HiltViewModel
import org.ibadalrahman.base.CoroutineDispatchers
import org.ibadalrahman.publicsector.main.domain.MainAction
import org.ibadalrahman.publicsector.main.domain.MainActivityInteractor
import org.ibadalrahman.publicsector.main.domain.MainResult
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    coroutineDispatchers: CoroutineDispatchers,
    interactor: MainActivityInteractor,
): BaseViewModel<MainState, MainIntention, MainViewAction, MainAction, MainResult>(
    savedStateHandle = savedStateHandle,
    coroutineDispatchers = coroutineDispatchers,
    initialState = MainState(),
    interactor = interactor
) {
    override fun router(intention: MainIntention): MviBoundary<MainViewAction, MainAction, MainResult> = action(MainAction.Noop)
    override fun reduce(result: MainResult) { }
}
