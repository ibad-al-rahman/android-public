package com.ibadalrahman.settings.domain.entity

import com.ibadalrahman.settings.presenter.entity.Language
import com.ibadalrahman.settings.presenter.entity.SettingsIntention

sealed interface SettingsAction {
    data class ChangeLanguage(val language: Language): SettingsAction
    data class ChangeTheme(val theme: Int): SettingsAction
}
