package org.ibadalrahman.forceupdate.presenter

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import org.ibadalrahman.base.CoroutineDispatchers
import org.ibadalrahman.forceupdate.domain.ForceUpdateInteractor
import org.ibadalrahman.forceupdate.domain.entity.ForceUpdateAction
import org.ibadalrahman.forceupdate.domain.entity.ForceUpdateResult
import org.ibadalrahman.forceupdate.presenter.entity.ForceUpdateIntention
import org.ibadalrahman.forceupdate.presenter.entity.ForceUpdateScreenState
import org.ibadalrahman.forceupdate.presenter.entity.ForceUpdateViewAction
import org.ibadalrahman.mvi.BaseViewModel
import org.ibadalrahman.mvi.MviBoundary
import javax.inject.Inject

@HiltViewModel
class ForceUpdateViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    coroutineDispatchers: CoroutineDispatchers,
    interactor: ForceUpdateInteractor,
) : BaseViewModel<
        ForceUpdateScreenState,
        ForceUpdateIntention,
        ForceUpdateViewAction,
        ForceUpdateAction,
        ForceUpdateResult
        >(
    savedStateHandle = savedStateHandle,
    coroutineDispatchers = coroutineDispatchers,
    initialState = ForceUpdateScreenState.Empty,
    interactor = interactor,
) {
    override fun router(
        intention: ForceUpdateIntention
    ): MviBoundary<ForceUpdateViewAction, ForceUpdateAction, ForceUpdateResult> =
        when (intention) {
            ForceUpdateIntention.OnAppear -> action(ForceUpdateAction.TrackShown)
            ForceUpdateIntention.OnTapUpdate -> action(ForceUpdateAction.RequestUpdate)
        }

    override fun reduce(result: ForceUpdateResult) {
        updateState { ForceUpdateReducer.reduce(prevState = this, result = result) }
    }

    override fun viewActionFrom(result: ForceUpdateResult): ForceUpdateViewAction? =
        when (result) {
            ForceUpdateResult.UpdateRequested -> ForceUpdateViewAction.LaunchUpdate
        }
}
