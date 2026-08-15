package org.ibadalrahman.publicsector.navigation

import java.net.URLEncoder

sealed class Screen(val route: String) {
    data object Adhkar: Screen("adhkar")
    data object AdhkarTour: Screen("adhkar/tour")
    data object Settings: Screen("settings")
    data object PrayerTimes: Screen("prayer-times")
    data object DeveloperSettings: Screen("developer-settings")

    // Settings sub-screens
    data object SettingsNotifications: Screen("settings/notifications")
    data object SettingsAppearance: Screen("settings/appearance")
    data object SettingsHelp: Screen("settings/help")
    data object SettingsPrayerTimesCalculation: Screen("settings/prayer-times-calculation")
    data object SettingsCalculationMethodSelection: Screen("settings/calculation-method-selection")
    data object SettingsLocationSearch: Screen("settings/location-search")
    data object SettingsAsrMethod: Screen("settings/asr-method")
    data object SettingsTimeAdjustments: Screen("settings/time-adjustments")

    fun createRouteWith(vararg args: Any, isNavigating: Boolean = false): String {
        var arguments = ""
        args.toList()
            .replaceBlank()
            .replaceSpecialCharacters()
            .forEach { arguments = arguments.plus(if (!isNavigating) "/{$it}" else "/$it") }
        return "$route$arguments"
    }
}

private fun List<Any>.replaceSpecialCharacters(): List<Any> = map {
    when (it) {
        is String -> URLEncoder.encode(it, "UTF-8").replace("+", "%20")
        else -> it
    }
}

private fun List<Any>.replaceBlank(): List<Any> = map {
    when (it) {
        is String -> it.ifBlank { "empty" }
        else -> it
    }
}
