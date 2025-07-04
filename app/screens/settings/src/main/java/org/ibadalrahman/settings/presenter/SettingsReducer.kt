package org.ibadalrahman.settings.presenter

import org.ibadalrahman.settings.domain.entity.SettingsResult
import org.ibadalrahman.settings.presenter.entity.SettingsScreenState

object SettingsReducer {
    fun reduce(
        prevState: SettingsScreenState,
        result: SettingsResult
    ): SettingsScreenState = prevState
}
