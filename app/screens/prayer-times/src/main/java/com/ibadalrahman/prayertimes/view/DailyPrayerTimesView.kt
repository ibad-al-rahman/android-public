package com.ibadalrahman.prayertimes.view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material.icons.outlined.Nightlight
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material.icons.outlined.WbTwilight
import androidx.compose.material3.DatePicker
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ibadalrahman.prayertimes.presenter.entity.Prayer
import com.ibadalrahman.prayertimes.presenter.entity.PrayerTimesIntention
import com.ibadalrahman.prayertimes.presenter.entity.PrayerTimesScreenState
import com.ibadalrahman.resources.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
            .padding(horizontal = 10.dp)
            .verticalScroll(ScrollState(0))
    ) {
        Spacer(
            modifier = Modifier.height(30.dp)
        )

        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 10.dp)
        ) {
            Text(
                text = stringResource(id = R.string.date),
                fontSize = 14.sp
            )

            FilledTonalButton(onClick = {
                intentionProcessor(PrayerTimesIntention.OnTapShowDatePicker)
            }) {
                DateText(date = state.date, format = "dd/MM/yyyy", fontSize = 14.sp)
            }

            if (state.isDatePickerVisible) {
                DatePickerModal(
                    onDateSelected = {},
                    onDismiss = {
                        intentionProcessor(PrayerTimesIntention.OnDismissDatePicker)
                    }
                )
            }
        }

        Text(
            text = stringResource(id = R.string.timings).uppercase(),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(vertical = 10.dp)
        )

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            PrayerTime(
                prayer = Prayer.FAJR,
                time = Date(),
                imageVector = Icons.Outlined.Nightlight,
                withDivider = true,
                verticalPadding = 8.dp
            )
            PrayerTime(
                prayer = Prayer.SUNRISE,
                time = Date(),
                imageVector = Icons.Outlined.WbTwilight,
                withDivider = true,
                verticalPadding = 8.dp
            )
            PrayerTime(
                prayer = Prayer.DHUHR,
                time = Date(),
                imageVector = Icons.Outlined.WbSunny,
                withDivider = true,
                verticalPadding = 8.dp
            )
            PrayerTime(
                prayer = Prayer.ASR,
                time = Date(),
                imageVector = Icons.Outlined.WbSunny,
                withDivider = true,
                verticalPadding = 8.dp
            )
            PrayerTime(
                prayer = Prayer.MAGHRIB,
                time = Date(),
                imageVector = Icons.Outlined.WbTwilight,
                withDivider = true,
                verticalPadding = 8.dp
            )
            PrayerTime(
                prayer = Prayer.ISHAA,
                time = Date(),
                imageVector = Icons.Outlined.Nightlight,
                verticalPadding = 8.dp
            )
        }
    }
}

@Composable
fun PrayerTime(
    prayer: Prayer,
    time: Date,
    imageVector: ImageVector,
    withDivider: Boolean = false,
    verticalPadding: Dp = 8.dp
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(
                    vertical = verticalPadding
                )
                .padding(horizontal = 10.dp)
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = prayer.name,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(size = 14.dp)
            )
            Text(
                text = localizedPrayerName(prayer),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.weight(1f))
            DateText(date = time, fontSize = 14.sp)
        }
        if (withDivider) {
            HorizontalDivider()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
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

@Composable
fun DateText(
    date: Date,
    format: String = "HH:mm a",
    fontSize: TextUnit = TextUnit.Unspecified
) {
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    val formattedDate = formatter.format(date)
    Text(text = formattedDate, fontSize = fontSize)
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
