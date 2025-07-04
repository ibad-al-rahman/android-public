package org.ibadalrahman.publicsector.navigation

import android.annotation.SuppressLint
import android.app.LocaleManager
import android.content.Intent
import android.os.Build
import android.os.LocaleList
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
import org.ibadalrahman.prayertimes.view.PrayerTimesRootScreen
import org.ibadalrahman.settings.view.SettingsRootScreen
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun NavigationGraph(
    navController: NavHostController,
    initialRoute: String
) {
    NavHost(navController = navController, startDestination = initialRoute) {
        addPrayerTimesScreen(navController = navController)
        addSettingsScreen(navController = navController)
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
            openContactUsLink = {
                uriHandler.openUri("https://www.ibad.org.lb/index.php/home/contactus")
            },
            openDonateLink = {
                uriHandler.openUri("https://www.ibad.org.lb/index.php/home/donationform")
            },
            changeLanguage = { languageCode ->
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    context.getSystemService(LocaleManager::class.java)
                        .applicationLocales = LocaleList.forLanguageTags(languageCode)
                }
                else {
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode))
                }
            },
            changeTheme = { theme ->
                when (theme) {
                    AppCompatDelegate.MODE_NIGHT_NO ->
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    AppCompatDelegate.MODE_NIGHT_YES ->
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    else ->
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
        )
    }
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

