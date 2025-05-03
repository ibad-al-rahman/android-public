package com.ibadalrahman.prayertimes.view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ibadalrahman.prayertimes.presenter.entity.Prayer
import com.ibadalrahman.prayertimes.presenter.entity.PrayerTimesIntention
import com.ibadalrahman.prayertimes.presenter.entity.PrayerTimesScreenState
import com.ibadalrahman.prayertimes.presenter.entity.WeekDay
import com.ibadalrahman.prayertimes.presenter.entity.WeekPrayerTimesState
import com.ibadalrahman.resources.R
import java.util.Date

@Composable
fun WeeklyPrayerTimesView(
    state: PrayerTimesScreenState,
    intentionProcessor: (intention: PrayerTimesIntention) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .padding(
                top = 30.dp,
                bottom = 0.dp,
                start = 20.dp,
                end = 20.dp
            )
    ) {
        Text(
            text = stringResource(id = R.string.timings).uppercase(),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(vertical = 10.dp)
        )

        Table(
            modifier = Modifier
                .fillMaxSize(),
            rowModifier = Modifier.background(MaterialTheme.colorScheme.background),
            columnCount = 7,
            rowCount = 8,
            cellContent = { columnIndex, rowIndex ->
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(8.dp)
                ) {
                    if (columnIndex == 0 && rowIndex == 0) {
                        Text(text = stringResource(id = R.string.week))
                    } else if (rowIndex == 0) {
                        Text(text = localizedPrayerName(tableIdxToPrayer(columnIndex)))
                    } else if (columnIndex == 0) {
                        Text(text = localizedWeekDay(tableIdxToWeekDay(rowIndex)))
                    } else {
                        PrayerTimeCell(
                            state = state.weekPrayerTimes,
                            prayer = tableIdxToPrayer(columnIndex),
                            weekDay = tableIdxToWeekDay(rowIndex)
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun Table(
    modifier: Modifier = Modifier,
    rowModifier: Modifier = Modifier,
    verticalLazyListState: LazyListState = rememberLazyListState(),
    horizontalScrollState: ScrollState = rememberScrollState(),
    columnCount: Int,
    rowCount: Int,
    beforeRow: (@Composable (rowIndex: Int) -> Unit)? = null,
    afterRow: (@Composable (rowIndex: Int) -> Unit)? = null,
    cellContent: @Composable (columnIndex: Int, rowIndex: Int) -> Unit
) {
    val columnWidths = remember { mutableStateMapOf<Int, Int>() }

    Box(modifier = modifier.then(Modifier.horizontalScroll(horizontalScrollState))) {
        LazyColumn(state = verticalLazyListState) {
            items(rowCount) { rowIndex ->
                Column {
                    beforeRow?.invoke(rowIndex)

                    Row(modifier = rowModifier) {
                        (0 until columnCount).forEach { columnIndex ->
                            Box(modifier = Modifier.layout { measurable, constraints ->
                                val placeable = measurable.measure(constraints)

                                val existingWidth = columnWidths[columnIndex] ?: 0
                                val maxWidth = maxOf(existingWidth, placeable.width)

                                if (maxWidth > existingWidth) {
                                    columnWidths[columnIndex] = maxWidth
                                }

                                layout(width = maxWidth, height = placeable.height) {
                                    placeable.placeRelative(0, 0)
                                }
                            }) {
                                cellContent(columnIndex, rowIndex)
                            }
                        }
                    }

                    afterRow?.invoke(rowIndex)
                }
            }
        }
    }
}

@Composable
fun PrayerTimeCell(
    state: WeekPrayerTimesState?,
    prayer: Prayer,
    weekDay: WeekDay
) {
    val dayPrayerTime = when(weekDay) {
        WeekDay.SAT -> state?.sat
        WeekDay.SUN -> state?.sun
        WeekDay.MON -> state?.mon
        WeekDay.TUE -> state?.tue
        WeekDay.WED -> state?.wed
        WeekDay.THU -> state?.thu
        WeekDay.FRI -> state?.fri
    }
    when(prayer) {
        Prayer.FAJR -> DateText(date = dayPrayerTime?.fajr ?: Date())
        Prayer.SUNRISE -> DateText(date = dayPrayerTime?.sunrise ?: Date())
        Prayer.DHUHR -> DateText(date = dayPrayerTime?.dhuhr ?: Date())
        Prayer.ASR -> DateText(date = dayPrayerTime?.asr ?: Date())
        Prayer.MAGHRIB -> DateText(date = dayPrayerTime?.maghrib ?: Date())
        Prayer.ISHAA -> DateText(date = dayPrayerTime?.ishaa ?: Date())
    }
}

// Starting at 1 because the first row and column are reserved for "week" string
@Composable
fun tableIdxToPrayer(idx: Int): Prayer = when(idx) {
    1 -> Prayer.FAJR
    2 -> Prayer.SUNRISE
    3 -> Prayer.DHUHR
    4 -> Prayer.ASR
    5 -> Prayer.MAGHRIB
    6 -> Prayer.ISHAA
    else -> Prayer.FAJR
}

// Starting at 1 because the first row and column are reserved for "week" string
@Composable
fun tableIdxToWeekDay(idx: Int): WeekDay = when(idx) {
    1 -> WeekDay.SAT
    2 -> WeekDay.SUN
    3 -> WeekDay.MON
    4 -> WeekDay.TUE
    5 -> WeekDay.WED
    6 -> WeekDay.THU
    7 -> WeekDay.FRI
    else -> WeekDay.SAT
}

@Composable
fun localizedWeekDay(weekDay: WeekDay): String = when(weekDay) {
    WeekDay.SAT -> stringResource(id = R.string.saturday)
    WeekDay.SUN -> stringResource(id = R.string.sunday)
    WeekDay.MON -> stringResource(id = R.string.monday)
    WeekDay.TUE -> stringResource(id = R.string.tuesday)
    WeekDay.WED -> stringResource(id = R.string.wednesday)
    WeekDay.THU -> stringResource(id = R.string.thursday)
    WeekDay.FRI -> stringResource(id = R.string.friday)
}
