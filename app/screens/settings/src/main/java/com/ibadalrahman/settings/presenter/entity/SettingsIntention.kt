package com.ibadalrahman.settings.presenter.entity

sealed interface SettingsIntention {
    data class ChangeLanguage(val language: Language): SettingsIntention
}

enum class Language(val code: String) {
    En("en"),
    Ar("ar")
}
