package com.ibadalrahman.settings.presenter.entity

sealed interface SettingsIntention {
    data class ChangeLanguage(val language: Language): SettingsIntention
    data class ChangeTheme(val theme: Int): SettingsIntention
}

enum class Language(val code: String) {
    En("en"),
    Ar("ar")
}
