package com.ibadalrahman.settings.domain.entity

sealed interface SettingsResult {
    object NoOp: SettingsResult
}
