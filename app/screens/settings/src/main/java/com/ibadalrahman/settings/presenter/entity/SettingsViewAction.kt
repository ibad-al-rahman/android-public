package com.ibadalrahman.settings.presenter.entity

sealed interface SettingsViewAction {
    data class ChangeLanguage(val language: Language) : SettingsViewAction
    data class ChangeTheme(val theme: Int): SettingsViewAction
}
