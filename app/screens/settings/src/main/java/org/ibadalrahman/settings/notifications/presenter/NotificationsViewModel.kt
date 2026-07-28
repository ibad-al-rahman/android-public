package org.ibadalrahman.settings.notifications.presenter

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import org.ibadalrahman.base.CoroutineDispatchers
import org.ibadalrahman.mvi.BaseViewModel
import org.ibadalrahman.mvi.MviBoundary
import org.ibadalrahman.settings.notifications.domain.NotificationsInteractor
import org.ibadalrahman.settings.notifications.domain.entity.NotificationsAction
import org.ibadalrahman.settings.notifications.domain.entity.NotificationsResult
import org.ibadalrahman.settings.notifications.presenter.entity.NotificationsIntention
import org.ibadalrahman.settings.notifications.presenter.entity.NotificationsScreenState
import org.ibadalrahman.settings.notifications.presenter.entity.NotificationsViewAction
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    coroutineDispatchers: CoroutineDispatchers,
    interactor: NotificationsInteractor,
) : BaseViewModel<
        NotificationsScreenState,
        NotificationsIntention,
        NotificationsViewAction,
        NotificationsAction,
        NotificationsResult
        >(
    savedStateHandle = savedStateHandle,
    coroutineDispatchers = coroutineDispatchers,
    initialState = NotificationsScreenState.Default,
    interactor = interactor,
) {
    // Intentions map directly to state results — nothing is persisted (UI shell).
    override fun router(
        intention: NotificationsIntention
    ): MviBoundary<NotificationsViewAction, NotificationsAction, NotificationsResult> =
        when (intention) {
            is NotificationsIntention.SetNotificationsEnabled ->
                result(NotificationsResult.NotificationsEnabled(intention.enabled))
            is NotificationsIntention.SetFajr -> result(NotificationsResult.Fajr(intention.enabled))
            is NotificationsIntention.SetDhuhr -> result(NotificationsResult.Dhuhr(intention.enabled))
            is NotificationsIntention.SetAsr -> result(NotificationsResult.Asr(intention.enabled))
            is NotificationsIntention.SetMaghrib ->
                result(NotificationsResult.Maghrib(intention.enabled))
            is NotificationsIntention.SetIshaa -> result(NotificationsResult.Ishaa(intention.enabled))
            is NotificationsIntention.SetMorningAdhkarEnabled ->
                result(NotificationsResult.MorningAdhkarEnabled(intention.enabled))
            is NotificationsIntention.SetEveningAdhkarEnabled ->
                result(NotificationsResult.EveningAdhkarEnabled(intention.enabled))
            is NotificationsIntention.SetMorningTime ->
                result(NotificationsResult.MorningTime(intention.hour, intention.minute))
            is NotificationsIntention.SetEveningTime ->
                result(NotificationsResult.EveningTime(intention.hour, intention.minute))
        }

    override fun reduce(result: NotificationsResult) {
        updateState { NotificationsReducer.reduce(prevState = this, result = result) }
    }
}
