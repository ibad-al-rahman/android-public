package com.ibadalrahman.settings.presenter.entity

sealed interface SettingsViewAction {
    data object ContactUs: SettingsViewAction
    data object Donate: SettingsViewAction
    data class ChangeLanguage(val language: Language): SettingsViewAction
    data class ChangeTheme(val theme: Int): SettingsViewAction
}
