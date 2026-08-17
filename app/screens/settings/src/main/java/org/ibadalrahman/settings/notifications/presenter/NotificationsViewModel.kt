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
    // Every intention persists via the interactor, which reloads and emits the fresh snapshot.
    override fun router(
        intention: NotificationsIntention
    ): MviBoundary<NotificationsViewAction, NotificationsAction, NotificationsResult> =
        when (intention) {
            NotificationsIntention.Load -> action(NotificationsAction.Load)
            is NotificationsIntention.SetNotificationsEnabled ->
                action(NotificationsAction.SetNotificationsEnabled(intention.enabled))
            is NotificationsIntention.SetFajr -> action(NotificationsAction.SetFajr(intention.enabled))
            is NotificationsIntention.SetDhuhr -> action(NotificationsAction.SetDhuhr(intention.enabled))
            is NotificationsIntention.SetAsr -> action(NotificationsAction.SetAsr(intention.enabled))
            is NotificationsIntention.SetMaghrib ->
                action(NotificationsAction.SetMaghrib(intention.enabled))
            is NotificationsIntention.SetIshaa -> action(NotificationsAction.SetIshaa(intention.enabled))
            is NotificationsIntention.SetMorningAdhkarEnabled ->
                action(NotificationsAction.SetMorningAdhkarEnabled(intention.enabled))
            is NotificationsIntention.SetEveningAdhkarEnabled ->
                action(NotificationsAction.SetEveningAdhkarEnabled(intention.enabled))
            is NotificationsIntention.SetMorningTime ->
                action(NotificationsAction.SetMorningTime(intention.hour, intention.minute))
            is NotificationsIntention.SetEveningTime ->
                action(NotificationsAction.SetEveningTime(intention.hour, intention.minute))
            NotificationsIntention.SendTestNotification ->
                action(NotificationsAction.SendTestNotification)
        }

    override fun reduce(result: NotificationsResult) {
        updateState { NotificationsReducer.reduce(prevState = this, result = result) }
    }
}
