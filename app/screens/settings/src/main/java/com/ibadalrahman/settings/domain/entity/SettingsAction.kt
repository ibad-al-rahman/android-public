package com.ibadalrahman.settings.domain.entity

import com.ibadalrahman.settings.presenter.entity.Language

sealed interface SettingsAction {
    data class ChangeLanguage(val language: Language) : SettingsAction
}
