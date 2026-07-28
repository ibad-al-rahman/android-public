package org.ibadalrahman.settings.notifications.presenter

import org.ibadalrahman.settings.notifications.domain.entity.NotificationsResult
import org.ibadalrahman.settings.notifications.presenter.entity.NotificationsScreenState

object NotificationsReducer {
    fun reduce(
        prevState: NotificationsScreenState,
        result: NotificationsResult
    ): NotificationsScreenState = when (result) {
        is NotificationsResult.NotificationsEnabled ->
            prevState.copy(notificationsEnabled = result.enabled)
        is NotificationsResult.Fajr -> prevState.copy(fajr = result.enabled)
        is NotificationsResult.Dhuhr -> prevState.copy(dhuhr = result.enabled)
        is NotificationsResult.Asr -> prevState.copy(asr = result.enabled)
        is NotificationsResult.Maghrib -> prevState.copy(maghrib = result.enabled)
        is NotificationsResult.Ishaa -> prevState.copy(ishaa = result.enabled)
        is NotificationsResult.MorningAdhkarEnabled ->
            prevState.copy(morningAdhkarEnabled = result.enabled)
        is NotificationsResult.EveningAdhkarEnabled ->
            prevState.copy(eveningAdhkarEnabled = result.enabled)
        is NotificationsResult.MorningTime ->
            prevState.copy(morningHour = result.hour, morningMinute = result.minute)
        is NotificationsResult.EveningTime ->
            prevState.copy(eveningHour = result.hour, eveningMinute = result.minute)
    }
}
