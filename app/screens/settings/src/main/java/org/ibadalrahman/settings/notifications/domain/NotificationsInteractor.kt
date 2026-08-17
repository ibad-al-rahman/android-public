package org.ibadalrahman.settings.notifications.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.ibadalrahman.mvi.BaseInteractor
import org.ibadalrahman.services.notifications.NotificationEventType
import org.ibadalrahman.services.notifications.NotificationPoster
import org.ibadalrahman.settings.notifications.domain.entity.NotificationsAction
import org.ibadalrahman.settings.notifications.domain.entity.NotificationsResult
import org.ibadalrahman.settings.repository.SettingsRepository
import org.ibadalrahman.settings.repository.data.domain.NotificationSettings
import javax.inject.Inject

/**
 * Persists each toggle/time change to [SettingsRepository] and reloads the full snapshot, so the
 * reducer always renders exactly what was stored. Saving also drives scheduling: the app observes
 * [SettingsRepository.notificationSettingsFlow] and re-arms the rolling alarm on every change.
 */
class NotificationsInteractor @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val notificationPoster: NotificationPoster,
) : BaseInteractor<NotificationsAction, NotificationsResult> {

    override suspend fun resultFrom(action: NotificationsAction): Flow<NotificationsResult> {
        if (action is NotificationsAction.SendTestNotification) {
            notificationPoster.post(NotificationEventType.FAJR)
            return flowOf(NotificationsResult.Loaded(settingsRepository.getNotificationSettings()))
        }

        val current = settingsRepository.getNotificationSettings()
        val updated = current.applying(action)
        if (updated != current) {
            settingsRepository.saveNotificationSettings(updated)
        }
        return flowOf(NotificationsResult.Loaded(settingsRepository.getNotificationSettings()))
    }

    private fun NotificationSettings.applying(action: NotificationsAction): NotificationSettings =
        when (action) {
            NotificationsAction.Load,
            NotificationsAction.SendTestNotification -> this
            is NotificationsAction.SetNotificationsEnabled -> copy(enabled = action.enabled)
            is NotificationsAction.SetFajr -> copy(fajr = action.enabled)
            is NotificationsAction.SetDhuhr -> copy(dhuhr = action.enabled)
            is NotificationsAction.SetAsr -> copy(asr = action.enabled)
            is NotificationsAction.SetMaghrib -> copy(maghrib = action.enabled)
            is NotificationsAction.SetIshaa -> copy(ishaa = action.enabled)
            is NotificationsAction.SetMorningAdhkarEnabled ->
                copy(morningAdhkarEnabled = action.enabled)
            is NotificationsAction.SetEveningAdhkarEnabled ->
                copy(eveningAdhkarEnabled = action.enabled)
            is NotificationsAction.SetMorningTime ->
                copy(morningHour = action.hour, morningMinute = action.minute)
            is NotificationsAction.SetEveningTime ->
                copy(eveningHour = action.hour, eveningMinute = action.minute)
        }
}
