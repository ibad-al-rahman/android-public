package org.ibadalrahman.settings.presenter.entity

import org.ibadalrahman.settings.repository.data.domain.Theme

sealed interface SettingsIntention {
    data object ContactUs: SettingsIntention
    data object Donate: SettingsIntention
    data object ClearCache: SettingsIntention
    data class ChangeLanguage(val language: Language): SettingsIntention
    data class ChangeTheme(val theme: Theme): SettingsIntention
}

enum class Language(val code: String) {
    En("en"),
    Ar("ar")
}
