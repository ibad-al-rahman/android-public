package com.ibadalrahman.settings.presenter

import com.ibadalrahman.settings.domain.entity.SettingsResult
import com.ibadalrahman.settings.presenter.entity.SettingsScreenState

object SettingsReducer {
    fun reduce(
        prevState: SettingsScreenState,
        result: SettingsResult
    ): SettingsScreenState = prevState
}
