package org.ibadalrahman.settings.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.ibadalrahman.settings.repository.data.domain.NotificationSettings
import org.ibadalrahman.settings.repository.data.domain.Theme
import org.ibadalrahman.settings.repository.data.local.SettingsLocalDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val localDatasource: SettingsLocalDataSource
): SettingsRepository {

    private val _themeFlow = MutableStateFlow(getTheme() ?: Theme.System)
    override val themeFlow: StateFlow<Theme> = _themeFlow.asStateFlow()

    private val _notificationSettingsFlow = MutableStateFlow(getNotificationSettings())
    override val notificationSettingsFlow: StateFlow<NotificationSettings> =
        _notificationSettingsFlow.asStateFlow()

    override fun saveTheme(theme: Theme) {
        localDatasource.saveTheme(theme)
        _themeFlow.value = theme
    }

    override fun getTheme(): Theme? = localDatasource.getTheme()

    override fun getNotificationSettings(): NotificationSettings =
        localDatasource.getNotificationSettings()

    override fun saveNotificationSettings(settings: NotificationSettings) {
        localDatasource.saveNotificationSettings(settings)
        _notificationSettingsFlow.value = settings
    }
}
