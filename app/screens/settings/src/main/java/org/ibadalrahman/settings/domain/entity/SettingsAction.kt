package org.ibadalrahman.settings.domain.entity

import org.ibadalrahman.settings.presenter.entity.Language
import org.ibadalrahman.settings.repository.data.domain.Theme

sealed interface SettingsAction {
    data object ContactUs: SettingsAction
    data object Donate: SettingsAction
    data object ClearCache: SettingsAction
    data object ShareApp: SettingsAction
    data class ChangeLanguage(val language: Language): SettingsAction
    data class ChangeTheme(val theme: Theme): SettingsAction
}
