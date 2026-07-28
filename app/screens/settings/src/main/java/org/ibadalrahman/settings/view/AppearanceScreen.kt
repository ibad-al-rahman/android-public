package org.ibadalrahman.settings.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.ibadalrahman.mvi.BaseScreen
import org.ibadalrahman.resources.R
import org.ibadalrahman.settings.presenter.SettingsViewModel
import org.ibadalrahman.settings.presenter.entity.SettingsIntention
import org.ibadalrahman.settings.repository.data.domain.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearanceScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit,
) {
    BaseScreen(
        viewModel = viewModel,
        viewActionProcessor = { /* theme change has no side effect to surface here */ }
    ) { _, intentionProcessor ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.appearance)) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.go),
                            )
                        }
                    }
                )
            }
        ) { padding ->
            LazyColumn(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                contentPadding = PaddingValues(20.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = padding.calculateTopPadding())
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                item {
                    SectionHeader(stringResource(R.string.theme))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        ThemeOption(R.string.system, Theme.System, intentionProcessor)
                        Spacer(Modifier.height(1.dp))
                        ThemeOption(R.string.light, Theme.Light, intentionProcessor)
                        Spacer(Modifier.height(1.dp))
                        ThemeOption(R.string.dark, Theme.Dark, intentionProcessor)
                    }
                }
            }
        }
    }
}

@Composable
private fun ThemeOption(
    labelRes: Int,
    theme: Theme,
    intentionProcessor: (SettingsIntention) -> Unit,
) {
    ListItem(
        headlineContent = { SettingsRowTitle(stringResource(labelRes)) },
        colors = listItemColors,
        modifier = Modifier.clickable {
            intentionProcessor(SettingsIntention.ChangeTheme(theme))
        },
    )
}
