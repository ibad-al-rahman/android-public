package org.ibadalrahman.settings.calculationmethod.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.ibadalrahman.miqat.TimeAdjustment
import org.ibadalrahman.mvi.BaseScreen
import org.ibadalrahman.resources.R
import org.ibadalrahman.settings.calculationmethod.presenter.CalculationMethodViewModel
import org.ibadalrahman.settings.calculationmethod.presenter.entity.CalculationMethodIntention
import org.ibadalrahman.settings.view.listItemColors

private const val MIN_ADJUSTMENT = -60L
private const val MAX_ADJUSTMENT = 60L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeAdjustmentsScreen(
    viewModel: CalculationMethodViewModel,
    onBack: () -> Unit,
) {
    BaseScreen(viewModel = viewModel, viewActionProcessor = { }) { state, intentionProcessor ->
        LoadOnce(intentionProcessor)
        val adjustments = state.method?.asAstronomical?.adjustments
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.time_adjustments)) },
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
            if (adjustments == null) return@Scaffold
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
                    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))) {
                        StepperRow(R.string.fajr, adjustments.fajr) {
                            intentionProcessor(setFajr(adjustments, it))
                        }
                        Spacer(Modifier.height(1.dp))
                        StepperRow(R.string.sunrise, adjustments.sunrise) {
                            intentionProcessor(setSunrise(adjustments, it))
                        }
                        Spacer(Modifier.height(1.dp))
                        StepperRow(R.string.dhuhr, adjustments.dhuhr) {
                            intentionProcessor(setDhuhr(adjustments, it))
                        }
                        Spacer(Modifier.height(1.dp))
                        StepperRow(R.string.asr, adjustments.asr) {
                            intentionProcessor(setAsr(adjustments, it))
                        }
                        Spacer(Modifier.height(1.dp))
                        StepperRow(R.string.maghrib, adjustments.maghrib) {
                            intentionProcessor(setMaghrib(adjustments, it))
                        }
                        Spacer(Modifier.height(1.dp))
                        StepperRow(R.string.ishaa, adjustments.ishaa) {
                            intentionProcessor(setIshaa(adjustments, it))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StepperRow(
    labelRes: Int,
    value: Long,
    onValueChange: (Long) -> Unit,
) {
    ListItem(
        headlineContent = {
            Text(
                text = stringResource(labelRes),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Medium,
                ),
            )
        },
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { onValueChange((value - 1).coerceAtLeast(MIN_ADJUSTMENT)) },
                    enabled = value > MIN_ADJUSTMENT,
                ) { Icon(Icons.Filled.Remove, contentDescription = "−") }
                Text(
                    text = "${formatMinutes(value)} ${stringResource(R.string.minutes_short)}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                )
                Spacer(Modifier.width(4.dp))
                IconButton(
                    onClick = { onValueChange((value + 1).coerceAtMost(MAX_ADJUSTMENT)) },
                    enabled = value < MAX_ADJUSTMENT,
                ) { Icon(Icons.Filled.Add, contentDescription = "+") }
            }
        },
        colors = listItemColors,
    )
}

private fun formatMinutes(value: Long): String = if (value > 0) "+$value" else value.toString()

// Field-wise copies wrapped as intentions — TimeAdjustment has no data-class copy() from the lib,
// so rebuild it explicitly from the current values.
private fun setFajr(a: TimeAdjustment, v: Long) =
    CalculationMethodIntention.SetAdjustments(TimeAdjustment(v, a.sunrise, a.dhuhr, a.asr, a.maghrib, a.ishaa))
private fun setSunrise(a: TimeAdjustment, v: Long) =
    CalculationMethodIntention.SetAdjustments(TimeAdjustment(a.fajr, v, a.dhuhr, a.asr, a.maghrib, a.ishaa))
private fun setDhuhr(a: TimeAdjustment, v: Long) =
    CalculationMethodIntention.SetAdjustments(TimeAdjustment(a.fajr, a.sunrise, v, a.asr, a.maghrib, a.ishaa))
private fun setAsr(a: TimeAdjustment, v: Long) =
    CalculationMethodIntention.SetAdjustments(TimeAdjustment(a.fajr, a.sunrise, a.dhuhr, v, a.maghrib, a.ishaa))
private fun setMaghrib(a: TimeAdjustment, v: Long) =
    CalculationMethodIntention.SetAdjustments(TimeAdjustment(a.fajr, a.sunrise, a.dhuhr, a.asr, v, a.ishaa))
private fun setIshaa(a: TimeAdjustment, v: Long) =
    CalculationMethodIntention.SetAdjustments(TimeAdjustment(a.fajr, a.sunrise, a.dhuhr, a.asr, a.maghrib, v))
