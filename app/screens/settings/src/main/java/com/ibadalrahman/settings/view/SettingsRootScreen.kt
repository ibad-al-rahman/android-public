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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import com.ibadalrahman.mvi.BaseScreen
import com.ibadalrahman.mvi.ObserveLifecycleEvents
import com.ibadalrahman.settings.presenter.SettingsViewModel
import com.ibadalrahman.resources.R
import com.ibadalrahman.settings.presenter.entity.Language
import com.ibadalrahman.settings.presenter.entity.SettingsViewAction

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsRootScreen(
    viewModel: SettingsViewModel,
    changeLanguage: (String) -> Unit,
    changeTheme: (Int) -> Unit
) {
    val layoutDirection = LocalLayoutDirection.current
    BaseScreen(
        viewModel = viewModel,
        viewActionProcessor = { viewAction ->
            when (viewAction) {
                is SettingsViewAction.ChangeLanguage -> changeLanguage(viewAction.language.code)
                is SettingsViewAction.ChangeTheme -> changeTheme(viewAction.theme)
            }
        }
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
            SettingsView(
                intentionProcessor = intentionProcessor,
                modifier = Modifier.padding(
                    top = padding.calculateTopPadding(),
                    start = padding.calculateStartPadding(layoutDirection),
                    end = padding.calculateEndPadding(layoutDirection),
                )
            )
        }
    }
}
