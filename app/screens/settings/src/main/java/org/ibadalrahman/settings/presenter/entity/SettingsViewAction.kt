package org.ibadalrahman.settings.presenter.entity

import org.ibadalrahman.settings.repository.data.domain.Theme

sealed interface SettingsViewAction {
    data object ContactUs: SettingsViewAction
    data object Donate: SettingsViewAction
    data class ChangeLanguage(val language: Language): SettingsViewAction
}
