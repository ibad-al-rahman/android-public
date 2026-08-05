package org.ibadalrahman.settings.repository

import kotlinx.coroutines.flow.StateFlow
import org.ibadalrahman.settings.repository.data.domain.NotificationSettings
import org.ibadalrahman.settings.repository.data.domain.Theme

interface SettingsRepository {
    val themeFlow: StateFlow<Theme>
    fun saveTheme(theme: Theme)
    fun getTheme(): Theme?

    /** Emits the current notification preferences and every subsequent change. */
    val notificationSettingsFlow: StateFlow<NotificationSettings>

    /** Synchronous snapshot of the persisted notification preferences — read by the scheduler. */
    fun getNotificationSettings(): NotificationSettings

    /** Persists [settings] and publishes them on [notificationSettingsFlow]. */
    fun saveNotificationSettings(settings: NotificationSettings)
}
