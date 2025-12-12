package org.ibadalrahman.settings.presenter.entity

sealed interface SettingsViewAction {
    data object ContactUs: SettingsViewAction
    data object Donate: SettingsViewAction
    data class Share(val text: String): SettingsViewAction
    data class ChangeLanguage(val language: Language): SettingsViewAction
}
