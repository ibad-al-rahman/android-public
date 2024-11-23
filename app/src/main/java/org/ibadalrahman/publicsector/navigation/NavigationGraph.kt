package org.ibadalrahman.publicsector.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavigationGraph(
    navController: NavHostController,
    initialRoute: String
) {
    NavHost(navController = navController, startDestination = initialRoute) {
        addPrayerTimesScreen(navController = navController)
        addAdhkarScreen(navController = navController)
        addSettingsScreen(navController = navController)
    }
}

fun NavGraphBuilder.addPrayerTimesScreen(navController: NavHostController) {
    composable(Screen.PrayerTimes.route) {
        Text("Prayer Times")
    }
}

fun NavGraphBuilder.addAdhkarScreen(navController: NavHostController) {
    composable(Screen.Adhkar.route) {
        Text("Adhkar")
    }
}

fun NavGraphBuilder.addSettingsScreen(navController: NavHostController) {
    composable(Screen.Settings.route) {
        Text("Settings")
    }
}
