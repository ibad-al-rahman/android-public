package org.ibadalrahman.settings.calculationmethod.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.ibadalrahman.miqat.repository.data.domain.MiqatCalculationMethod
import org.ibadalrahman.mvi.BaseScreen
import org.ibadalrahman.resources.R
import org.ibadalrahman.settings.calculationmethod.presenter.CalculationMethodViewModel
import org.ibadalrahman.settings.calculationmethod.presenter.entity.CalculationMethodIntention
import org.ibadalrahman.settings.calculationmethod.presenter.entity.CalculationMethodScreenState
import org.ibadalrahman.settings.calculationmethod.presenter.entity.CalculationMethodViewAction
import org.ibadalrahman.settings.view.NavigationRow
import org.ibadalrahman.settings.view.SectionHeader
import org.ibadalrahman.settings.view.listItemColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerTimesCalculationScreen(
    viewModel: CalculationMethodViewModel,
    onBack: () -> Unit,
    openAstronomicalMethod: () -> Unit,
    openAsrMethod: () -> Unit,
    openTimeAdjustments: () -> Unit,
) {
    BaseScreen(
        viewModel = viewModel,
        viewActionProcessor = { viewAction ->
            when (viewAction) {
                CalculationMethodViewAction.OpenAstronomicalMethod -> openAstronomicalMethod()
            }
        }
    ) { state, intentionProcessor ->
        LoadOnce(intentionProcessor)
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.prayer_times_calculation_method)) },
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
            Content(
                state = state,
                intentionProcessor = intentionProcessor,
                openAstronomicalMethod = openAstronomicalMethod,
                openAsrMethod = openAsrMethod,
                openTimeAdjustments = openTimeAdjustments,
                modifier = Modifier.padding(top = padding.calculateTopPadding()),
            )
        }
    }
}

@Composable
private fun Content(
    state: CalculationMethodScreenState,
    intentionProcessor: (CalculationMethodIntention) -> Unit,
    openAstronomicalMethod: () -> Unit,
    openAsrMethod: () -> Unit,
    openTimeAdjustments: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        contentPadding = PaddingValues(20.dp),
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        item {
            ModePicker(
                isAstronomical = state.isAstronomical,
                onAstronomical = {
                    intentionProcessor(CalculationMethodIntention.SelectAstronomical)
                },
                onPrecomputed = {
                    intentionProcessor(CalculationMethodIntention.SelectPrecomputed)
                },
            )
        }

        item { Spacer(Modifier.height(24.dp)) }

        if (state.method is MiqatCalculationMethod.Astronomical) {
            item {
                val config = state.method.config
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    NavigationRow(
                        text = stringResource(R.string.astronomical_method),
                        icon = Icons.Filled.Tune,
                        onClick = openAstronomicalMethod,
                        badge = stringResource(config.method.labelRes),
                    )
                    Spacer(Modifier.height(1.dp))
                    NavigationRow(
                        text = stringResource(R.string.asr_method),
                        icon = Icons.Filled.WbSunny,
                        onClick = openAsrMethod,
                        badge = stringResource(config.mazhab.labelRes),
                    )
                    Spacer(Modifier.height(1.dp))
                    NavigationRow(
                        text = stringResource(R.string.time_adjustments),
                        icon = Icons.Filled.AccessTime,
                        onClick = openTimeAdjustments,
                    )
                }
            }
        } else {
            item {
                SectionHeader(stringResource(R.string.precomputed_method))
                Text(
                    text = stringResource(R.string.dar_el_fatwa_beirut),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }
        }
    }
}

@Composable
private fun ModePicker(
    isAstronomical: Boolean,
    onAstronomical: () -> Unit,
    onPrecomputed: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val currentLabel = if (isAstronomical) {
        R.string.astronomical_mode
    } else {
        R.string.precomputed_method
    }

    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))) {
        ListItem(
            headlineContent = {
                Text(stringResource(R.string.prayer_times_calculation_method))
            },
            trailingContent = {
                Box {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(currentLabel),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = stringResource(R.string.select),
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.astronomical_mode)) },
                            onClick = {
                                expanded = false
                                onAstronomical()
                            },
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.precomputed_method)) },
                            onClick = {
                                expanded = false
                                onPrecomputed()
                            },
                        )
                    }
                }
            },
            colors = listItemColors,
            modifier = Modifier.clickable { expanded = !expanded },
        )
    }
}
