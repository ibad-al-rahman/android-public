package com.ibadalrahman.settings.domain.entity

import com.ibadalrahman.settings.presenter.entity.Language

sealed interface SettingsResult {
    data class LanguageChanged(val language: Language) : SettingsResult
}
