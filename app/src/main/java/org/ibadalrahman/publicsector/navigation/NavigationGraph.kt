package org.ibadalrahman.publicsector.navigation

import android.annotation.SuppressLint
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.ibadalrahman.prayertimes.view.PrayerTimesRootScreen
import org.ibadalrahman.publicsector.main.view.SettingsContent
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
        PrayerTimesRootScreen(viewModel = hiltViewModel())
    }
}

fun NavGraphBuilder.addSettingsScreen(navController: NavHostController) {
    composable(Screen.Settings.route) {
        SettingsContent()
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

