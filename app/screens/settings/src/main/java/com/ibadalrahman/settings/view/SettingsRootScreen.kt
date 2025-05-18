package com.ibadalrahman.settings.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ibadalrahman.mvi.BaseScreen
import com.ibadalrahman.mvi.ObserveLifecycleEvents
import com.ibadalrahman.settings.presenter.SettingsViewModel

@Composable
fun SettingsRootScreen(viewModel: SettingsViewModel) {
    BaseScreen(
        viewModel = viewModel,
        viewActionProcessor = { _ -> }
    ) { state, intentionProcessor ->
        ObserveLifecycleEvents ()
        Text("Settings")
    }
}
