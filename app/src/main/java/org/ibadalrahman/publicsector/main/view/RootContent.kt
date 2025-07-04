package org.ibadalrahman.publicsector.main.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.ibadalrahman.publicsector.main.presenter.MainActivityViewModel
import org.ibadalrahman.publicsector.navigation.NavigationGraph
import org.ibadalrahman.publicsector.navigation.Screen
import org.ibadalrahman.publicsector.navigation.TabBarItem
import org.ibadalrahman.publicsector.ui.theme.AppTheme
import org.ibadalrahman.resources.R

@Composable
fun RootContent(
    navController: NavHostController = rememberNavController(),
    mainViewModel: MainActivityViewModel
) {
    val tabBarItems = remember {
        arrayOf(
            TabBarItem(
                route = Screen.PrayerTimes.route,
                icon = Icons.Outlined.CalendarMonth,
                selectedIcon = Icons.Filled.CalendarMonth,
                stringResourceId = R.string.timings
            ),
            TabBarItem(
                route = Screen.Settings.route,
                icon = Icons.Outlined.Settings,
                selectedIcon = Icons.Filled.Settings,
                stringResourceId = R.string.settings
            ),
        ).toList()
    }

    Content(
        navController = navController,
        initialRoute = Screen.PrayerTimes.route,
        tabBarItems = tabBarItems
    )
}

@Composable
fun Content(
    navController: NavHostController,
    initialRoute: String,
    tabBarItems: List<TabBarItem>
) {
    AppTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                val backStackEntryState by navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntryState?.destination?.route ?: return@Scaffold
                val isTabBarRoute = tabBarItems.any { it.route == currentRoute }
                if (isTabBarRoute) {
                    TabBar(
                        tabBarItems = tabBarItems,
                        selectedRoute = currentRoute,
                        onNavigationSelected = { route ->
                            if (currentRoute != route) {
                                navController.navigate(route) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(Screen.PrayerTimes.route) { saveState = false }
                                }
                            }
                        }
                    )
                }
            }
        ) { padding ->
            Box(
                modifier = Modifier.fillMaxSize().padding(padding)
            ) {
                NavigationGraph(navController = navController, initialRoute = initialRoute)
            }
        }
    }
}
