package com.ibadalrahman.settings.domain.entity

import com.ibadalrahman.settings.presenter.entity.Language

sealed interface SettingsResult {
    data object NoOp: SettingsResult
    data object ContactUs: SettingsResult
    data object Donate: SettingsResult
    data class LanguageChanged(val language: Language): SettingsResult
    data class ThemeChanged(val theme: Int): SettingsResult
}
