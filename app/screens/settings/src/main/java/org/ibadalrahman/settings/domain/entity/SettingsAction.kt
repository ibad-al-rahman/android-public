package org.ibadalrahman.settings.domain.entity

import org.ibadalrahman.settings.presenter.entity.Language

sealed interface SettingsAction {
    data object ContactUs: SettingsAction
    data object Donate: SettingsAction
    data object ClearCache: SettingsAction
    data class ChangeLanguage(val language: Language): SettingsAction
    data class ChangeTheme(val theme: Int): SettingsAction
}
