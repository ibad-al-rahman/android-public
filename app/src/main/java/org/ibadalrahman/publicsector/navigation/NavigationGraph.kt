package org.ibadalrahman.publicsector.navigation

import android.Manifest
import android.annotation.SuppressLint
import android.app.LocaleManager
import android.content.Intent
import android.os.Build
import android.os.LocaleList
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import org.ibadalrahman.adhkar.collection.view.AdhkarCollectionScreen
import org.ibadalrahman.adhkar.tour.presenter.AdhkarTourViewModel
import org.ibadalrahman.adhkar.tour.view.AdhkarTourScreen
import org.ibadalrahman.prayertimes.view.PrayerTimesRootScreen
import org.ibadalrahman.settings.calculationmethod.view.AsrMethodScreen
import org.ibadalrahman.settings.calculationmethod.view.CalculationMethodSelectionScreen
import org.ibadalrahman.settings.calculationmethod.view.LocationSearchScreen
import org.ibadalrahman.settings.calculationmethod.view.PrayerTimesCalculationScreen
import org.ibadalrahman.settings.calculationmethod.view.TimeAdjustmentsScreen
import org.ibadalrahman.settings.help.view.HelpScreen
import org.ibadalrahman.settings.notifications.view.NotificationsScreen
import org.ibadalrahman.settings.view.AppearanceScreen
import org.ibadalrahman.settings.view.SettingsRootScreen
import java.text.SimpleDateFormat
import java.util.Date

private const val CONTACT_US_URL = "https://www.ibad.org.lb/index.php/home/contactus"
private const val DONATE_URL = "https://www.ibad.org.lb/index.php/home/donationform"
private const val PLAY_STORE_URL =
    "https://play.google.com/store/apps/details?id=org.ibadalrahman.publicsector"

@Composable
fun NavigationGraph(
    navController: NavHostController,
    initialRoute: String
) {
    NavHost(navController = navController, startDestination = initialRoute) {
        addPrayerTimesScreen(navController = navController)
        addAdhkarScreens(navController = navController)
        addSettingsScreen(navController = navController)
        addSettingsSubScreens(navController = navController)
    }
}

fun NavGraphBuilder.addAdhkarScreens(navController: NavHostController) {
    composable(Screen.Adhkar.route) {
        AdhkarCollectionScreen(
            viewModel = hiltViewModel(),
            openTour = { collection ->
                navController.navigate(
                    Screen.AdhkarTour.createRouteWith(collection.slug, isNavigating = true)
                )
            },
        )
    }

    composable(
        route = Screen.AdhkarTour.createRouteWith(AdhkarTourViewModel.COLLECTION_ARG),
        arguments = listOf(
            navArgument(AdhkarTourViewModel.COLLECTION_ARG) { type = NavType.StringType },
        ),
    ) {
        AdhkarTourScreen(
            viewModel = hiltViewModel(),
            onClose = { navController.popBackStack() },
        )
    }
}

fun NavGraphBuilder.addPrayerTimesScreen(
    navController: NavHostController
) {
    composable(Screen.PrayerTimes.route) {
        val context = LocalContext.current
        PrayerTimesRootScreen(
            viewModel = hiltViewModel(),
            onShare = {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, it)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                context.startActivity(shareIntent)
            }
        )
    }
}

fun NavGraphBuilder.addSettingsScreen(navController: NavHostController) {
    composable(Screen.Settings.route) {
        val context = LocalContext.current
        val uriHandler = LocalUriHandler.current

        SettingsRootScreen(
            viewModel = hiltViewModel(),
            openContactUsLink = { uriHandler.openUri(CONTACT_US_URL) },
            openDonateLink = { uriHandler.openUri(DONATE_URL) },
            changeLanguage = { languageCode -> context.applyLanguage(languageCode) },
            onShare = { text -> context.shareText(text) },
            openNotifications = {
                navController.navigate(Screen.SettingsNotifications.route)
            },
            openPrayerTimesCalculation = {
                navController.navigate(Screen.SettingsPrayerTimesCalculation.route)
            },
            openAppearance = { navController.navigate(Screen.SettingsAppearance.route) },
            openHelp = { navController.navigate(Screen.SettingsHelp.route) },
            openRateUs = { uriHandler.openUri(PLAY_STORE_URL) },
        )
    }
}

fun NavGraphBuilder.addSettingsSubScreens(navController: NavHostController) {
    composable(Screen.SettingsNotifications.route) {
        val permissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { /* granted-or-not is reflected by the system; nothing to do here */ }

        NotificationsScreen(
            viewModel = hiltViewModel(),
            onBack = { navController.popBackStack() },
            onEnableNotifications = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            },
        )
    }

    composable(Screen.SettingsAppearance.route) {
        AppearanceScreen(
            viewModel = hiltViewModel(),
            onBack = { navController.popBackStack() },
        )
    }

    composable(Screen.SettingsHelp.route) {
        val uriHandler = LocalUriHandler.current
        HelpScreen(
            onBack = { navController.popBackStack() },
            onContactUs = { uriHandler.openUri(CONTACT_US_URL) },
        )
    }

    composable(Screen.SettingsPrayerTimesCalculation.route) {
        PrayerTimesCalculationScreen(
            viewModel = hiltViewModel(),
            onBack = { navController.popBackStack() },
            openAstronomicalMethod = {
                navController.navigate(Screen.SettingsCalculationMethodSelection.route)
            },
            openAsrMethod = { navController.navigate(Screen.SettingsAsrMethod.route) },
            openTimeAdjustments = { navController.navigate(Screen.SettingsTimeAdjustments.route) },
        )
    }

    composable(Screen.SettingsCalculationMethodSelection.route) {
        CalculationMethodSelectionScreen(
            viewModel = hiltViewModel(),
            onBack = { navController.popBackStack() },
            openLocationSearch = { navController.navigate(Screen.SettingsLocationSearch.route) },
        )
    }

    composable(Screen.SettingsLocationSearch.route) {
        LocationSearchScreen(
            viewModel = hiltViewModel(),
            onLocationSelected = { navController.popBackStack() },
            onBack = { navController.popBackStack() },
        )
    }

    composable(Screen.SettingsAsrMethod.route) {
        AsrMethodScreen(
            viewModel = hiltViewModel(),
            onBack = { navController.popBackStack() },
        )
    }

    composable(Screen.SettingsTimeAdjustments.route) {
        TimeAdjustmentsScreen(
            viewModel = hiltViewModel(),
            onBack = { navController.popBackStack() },
        )
    }
}

private fun android.content.Context.applyLanguage(languageCode: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSystemService(LocaleManager::class.java).applicationLocales =
            LocaleList.forLanguageTags(languageCode)
    } else {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode))
    }
}

private fun android.content.Context.shareText(text: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    startActivity(Intent.createChooser(sendIntent, null))
}

// date picker modal
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val selectedDate = datePickerState.selectedDateMillis?.let {
                    convertMillisToDate(it)
                } ?: ""
                onDateSelected(selectedDate)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@SuppressLint("SimpleDateFormat")
private fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy")
    return formatter.format(Date(millis))
}

