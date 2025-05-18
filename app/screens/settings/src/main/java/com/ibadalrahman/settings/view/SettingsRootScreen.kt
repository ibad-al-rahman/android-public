package com.ibadalrahman.settings.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import com.ibadalrahman.mvi.BaseScreen
import com.ibadalrahman.mvi.ObserveLifecycleEvents
import com.ibadalrahman.settings.presenter.SettingsViewModel
import com.ibadalrahman.resources.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsRootScreen(viewModel: SettingsViewModel) {
    val layoutDirection = LocalLayoutDirection.current
    BaseScreen(
        viewModel = viewModel,
        viewActionProcessor = { _ -> }
    ) { state, intentionProcessor ->
        ObserveLifecycleEvents ()
        Scaffold(
            topBar = {
                TopAppBar(title = {
                    Text(
                        text = stringResource(id = R.string.settings),
                        style = MaterialTheme.typography.displayLarge
                    )
                })
            }
        ) { padding ->
            SettingsView(modifier = Modifier.padding(
                top = padding.calculateTopPadding(),
                start = padding.calculateStartPadding(layoutDirection),
                end = padding.calculateEndPadding(layoutDirection),
            ))
        }
    }
}
