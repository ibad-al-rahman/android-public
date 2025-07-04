package org.ibadalrahman.settings.presenter.entity

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
@Immutable
sealed interface SettingsScreenState {
    data object Empty : SettingsScreenState
}
