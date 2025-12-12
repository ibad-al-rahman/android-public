package org.ibadalrahman.settings.domain.entity

import org.ibadalrahman.settings.repository.data.domain.Theme
import org.ibadalrahman.settings.presenter.entity.Language

sealed interface SettingsResult {
    data object NoOp: SettingsResult
    data object ContactUs: SettingsResult
    data object Donate: SettingsResult
    data class ShareApp(val text: String): SettingsResult
    data class LanguageChanged(val language: Language): SettingsResult
    data class ThemeChanged(val theme: Theme): SettingsResult
}
