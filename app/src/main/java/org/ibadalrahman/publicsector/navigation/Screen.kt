package org.ibadalrahman.publicsector.navigation

import java.net.URLEncoder

sealed class Screen(val route: String) {
    data object Adhkar: Screen("adhkar")
    data object Settings: Screen("settings")
    data object PrayerTimes: Screen("prayer-times")
    data object DeveloperSettings: Screen("developer-settings")

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
