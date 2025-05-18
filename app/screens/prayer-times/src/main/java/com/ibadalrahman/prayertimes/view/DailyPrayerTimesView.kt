package com.ibadalrahman.prayertimes.view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Nightlight
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material.icons.outlined.WbTwilight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import com.ibadalrahman.prayertimes.presenter.entity.Prayer
import com.ibadalrahman.prayertimes.presenter.entity.PrayerTimesIntention
import com.ibadalrahman.prayertimes.presenter.entity.PrayerTimesScreenState
import com.ibadalrahman.resources.R
import org.ibadalrahman.fp.safeLet
import java.text.DateFormat
import java.util.Date

@Composable
fun DailyPrayerTimesView(
    state: PrayerTimesScreenState,
    intentionProcessor: (intention: PrayerTimesIntention) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .padding(horizontal = 20.dp)
            .verticalScroll(ScrollState(0))
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Text(
                text = stringResource(id = R.string.date),
                style = MaterialTheme.typography.bodyLarge,
            )

            FilledTonalButton(
                onClick = { intentionProcessor(PrayerTimesIntention.OnTapShowDatePicker) },
                shape = RoundedCornerShape(12.dp),
            ) {
                DateText(
                    date = state.date,
                    format = DateFormat.getDateInstance(DateFormat.MEDIUM)
                )
            }

            if (state.isDatePickerVisible) {
                DatePickerModal(
                    onDateSelected = {
                        val date = it ?: Date()
                        intentionProcessor(PrayerTimesIntention.OnDateSelected(date))
                    },
                    onDismiss = {
                        intentionProcessor(PrayerTimesIntention.OnDismissDatePicker)
                    }
                )
            }
        }
        safeLet(state.prayerTimes?.hijriDate) { hijriDate ->
            Text(
                text = hijriDate,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.padding(top = 8.dp, start = 20.dp)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = stringResource(id = R.string.timings).uppercase(),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
        )

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(30.dp)
                        .padding(10.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            safeLet(
                state.prayerTimes?.fajr,
                state.prayerTimes?.sunrise,
                state.prayerTimes?.dhuhr,
                state.prayerTimes?.asr,
                state.prayerTimes?.maghrib,
                state.prayerTimes?.ishaa
            ) { fajr, sunrise, dhuhr, asr, maghrib, ishaa ->
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    PrayerTime(
                        prayer = Prayer.FAJR,
                        time = fajr,
                        imageVector = Icons.Outlined.Nightlight,
                        withDivider = true
                    )
                    PrayerTime(
                        prayer = Prayer.SUNRISE,
                        time = sunrise,
                        imageVector = Icons.Outlined.WbTwilight,
                        withDivider = true
                    )
                    PrayerTime(
                        prayer = Prayer.DHUHR,
                        time = dhuhr,
                        imageVector = Icons.Outlined.WbSunny,
                        withDivider = true
                    )
                    PrayerTime(
                        prayer = Prayer.ASR,
                        time = asr,
                        imageVector = Icons.Outlined.WbSunny,
                        withDivider = true
                    )
                    PrayerTime(
                        prayer = Prayer.MAGHRIB,
                        time = maghrib,
                        imageVector = Icons.Outlined.WbTwilight,
                        withDivider = true
                    )
                    PrayerTime(
                        prayer = Prayer.ISHAA,
                        time = ishaa,
                        imageVector = Icons.Outlined.Nightlight
                    )
                }
            }
        }

        safeLet(state.event) { event ->
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = stringResource(id = R.string.events).uppercase(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
            )
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = event,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Medium
                        ),
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        safeLet(state.weekPrayerTimes?.hadithState) { hadith ->
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = stringResource(id = R.string.hadith).uppercase(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
            )
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = hadith.hadith,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Medium,
                            textDirection = TextDirection.Rtl
                        ),
                    )
                }
            }

            hadith.note?.let {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelLarge.copy(
                            textDirection = TextDirection.Rtl,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        // scrollable leeway
        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun PrayerTime(
    prayer: Prayer,
    time: Date,
    imageVector: ImageVector,
    withDivider: Boolean = false
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = prayer.name,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(size = 20.dp)
            )
            Text(
                text = localizedPrayerName(prayer),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Medium
                ),
            )
            Spacer(modifier = Modifier.weight(1f))
            DateText(date = time, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        if (withDivider) {
            val color = if (isSystemInDarkTheme()) {
                Color(0x223A3A3C)
            } else {
                Color(0x22D1D1D6)
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = color
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Date?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val date = datePickerState.selectedDateMillis?.let { millis ->
                    Date(millis)
                } ?: Date()
                onDateSelected(date)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            headlineContentColor = MaterialTheme.colorScheme.onSurface,
            weekdayContentColor = MaterialTheme.colorScheme.primary,
            subheadContentColor = MaterialTheme.colorScheme.onSurface,
            yearContentColor = MaterialTheme.colorScheme.onSurface,
            selectedYearContentColor = MaterialTheme.colorScheme.onPrimary,
            selectedYearContainerColor = MaterialTheme.colorScheme.primary,
            dayContentColor = MaterialTheme.colorScheme.onSurface,
            selectedDayContentColor = MaterialTheme.colorScheme.onPrimary,
            selectedDayContainerColor = MaterialTheme.colorScheme.primary,
            todayContentColor = MaterialTheme.colorScheme.primary,
            todayDateBorderColor = MaterialTheme.colorScheme.primary
        )
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun DateText(
    date: Date,
    format: DateFormat? = null,
    color: Color? = null,
) {
    val formatter = format ?: DateFormat.getTimeInstance(DateFormat.SHORT)
    val formattedDate = formatter.format(date)
    Text(
        text = formattedDate,
        style = MaterialTheme.typography.bodyLarge.copy(
            color = color ?: MaterialTheme.colorScheme.onPrimaryContainer
        ),
    )
}

@Composable
fun localizedPrayerName(prayer: Prayer): String = when(prayer) {
        Prayer.FAJR -> stringResource(R.string.fajr)
        Prayer.SUNRISE -> stringResource(R.string.sunrise)
        Prayer.DHUHR -> stringResource(R.string.dhuhr)
        Prayer.ASR -> stringResource(R.string.asr)
        Prayer.MAGHRIB -> stringResource(R.string.maghrib)
        Prayer.ISHAA -> stringResource(R.string.ishaa)
}
