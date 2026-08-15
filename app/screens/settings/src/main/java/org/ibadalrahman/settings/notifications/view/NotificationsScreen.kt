package org.ibadalrahman.settings.notifications.view

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.ibadalrahman.mvi.BaseScreen
import org.ibadalrahman.mvi.ObserveLifecycleEvents
import org.ibadalrahman.resources.R
import org.ibadalrahman.settings.notifications.presenter.NotificationsViewModel
import org.ibadalrahman.settings.notifications.presenter.entity.NotificationsIntention
import org.ibadalrahman.settings.notifications.presenter.entity.NotificationsScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    viewModel: NotificationsViewModel,
    onBack: () -> Unit,
    onEnableNotifications: () -> Unit = {},
) {
    BaseScreen(
        viewModel = viewModel,
        viewActionProcessor = { /* no side effects yet */ }
    ) { state, intentionProcessor ->
        ObserveLifecycleEvents(
            onResume = { intentionProcessor(NotificationsIntention.Load) }
        )
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.notifications)) },
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
            NotificationsContent(
                state = state,
                intentionProcessor = intentionProcessor,
                onEnableNotifications = onEnableNotifications,
                modifier = Modifier.padding(top = padding.calculateTopPadding()),
            )
        }
    }
}

@Composable
private fun NotificationsContent(
    state: NotificationsScreenState,
    intentionProcessor: (NotificationsIntention) -> Unit,
    onEnableNotifications: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val enabled = state.notificationsEnabled
    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        contentPadding = PaddingValues(20.dp),
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        // Master toggle
        item {
            Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))) {
                SwitchRow(
                    label = stringResource(R.string.enable_notifications),
                    checked = state.notificationsEnabled,
                    onCheckedChange = { checked ->
                        intentionProcessor(NotificationsIntention.SetNotificationsEnabled(checked))
                        if (checked) onEnableNotifications()
                    },
                )
            }
        }

        // Prayer notifications
        item {
            Spacer(Modifier.height(40.dp))
            SectionHeader(stringResource(R.string.prayer_notifications))
            Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))) {
                SwitchRow(stringResource(R.string.fajr), state.fajr, enabled) {
                    intentionProcessor(NotificationsIntention.SetFajr(it))
                }
                Spacer(Modifier.height(1.dp))
                SwitchRow(stringResource(R.string.dhuhr), state.dhuhr, enabled) {
                    intentionProcessor(NotificationsIntention.SetDhuhr(it))
                }
                Spacer(Modifier.height(1.dp))
                SwitchRow(stringResource(R.string.asr), state.asr, enabled) {
                    intentionProcessor(NotificationsIntention.SetAsr(it))
                }
                Spacer(Modifier.height(1.dp))
                SwitchRow(stringResource(R.string.maghrib), state.maghrib, enabled) {
                    intentionProcessor(NotificationsIntention.SetMaghrib(it))
                }
                Spacer(Modifier.height(1.dp))
                SwitchRow(stringResource(R.string.ishaa), state.ishaa, enabled) {
                    intentionProcessor(NotificationsIntention.SetIshaa(it))
                }
            }
        }

        // Morning adhkar
        item {
            Spacer(Modifier.height(40.dp))
            SectionHeader(stringResource(R.string.morning_adhkar_notification))
            Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))) {
                SwitchRow(
                    stringResource(R.string.morning_adhkar),
                    state.morningAdhkarEnabled,
                    enabled,
                ) { intentionProcessor(NotificationsIntention.SetMorningAdhkarEnabled(it)) }
                Spacer(Modifier.height(1.dp))
                TimeRow(
                    hour = state.morningHour,
                    minute = state.morningMinute,
                    enabled = enabled && state.morningAdhkarEnabled,
                ) { hour, minute ->
                    intentionProcessor(NotificationsIntention.SetMorningTime(hour, minute))
                }
            }
        }

        // Evening adhkar
        item {
            Spacer(Modifier.height(40.dp))
            SectionHeader(stringResource(R.string.evening_adhkar_notification))
            Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))) {
                SwitchRow(
                    stringResource(R.string.evening_adhkar),
                    state.eveningAdhkarEnabled,
                    enabled,
                ) { intentionProcessor(NotificationsIntention.SetEveningAdhkarEnabled(it)) }
                Spacer(Modifier.height(1.dp))
                TimeRow(
                    hour = state.eveningHour,
                    minute = state.eveningMinute,
                    enabled = enabled && state.eveningAdhkarEnabled,
                ) { hour, minute ->
                    intentionProcessor(NotificationsIntention.SetEveningTime(hour, minute))
                }
            }
        }
    }
}

@Composable
private fun SwitchRow(
    label: String,
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit,
) {
    ListItem(
        headlineContent = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Medium,
                ),
            )
        },
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = enabled,
                thumbContent = {
                    Icon(
                        imageVector = if (checked) Icons.Filled.Check else Icons.Filled.Close,
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize),
                    )
                },
                colors = SwitchDefaults.colors(
                    uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                    uncheckedBorderColor = MaterialTheme.colorScheme.outline,
                ),
            )
        },
        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.background),
        modifier = Modifier.clickable(enabled = enabled) { onCheckedChange(!checked) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimeRow(
    hour: Int,
    minute: Int,
    enabled: Boolean,
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

    ListItem(
        headlineContent = {
            Text(
                text = stringResource(R.string.time),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Medium,
                ),
            )
        },
        trailingContent = {
            Text(
                text = "%02d:%02d".format(hour, minute),
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.background),
        modifier = Modifier.clickable(enabled = enabled) { showDialog = true },
    )

    if (showDialog) {
        val timeState = rememberTimePickerState(initialHour = hour, initialMinute = minute)
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    onTimeSelected(timeState.hour, timeState.minute)
                    showDialog = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel") }
            },
            text = { TimePicker(state = timeState) },
        )
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
    )
}
