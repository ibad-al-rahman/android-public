package org.ibadalrahman.prayertimes.view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import org.ibadalrahman.prayertimes.presenter.entity.Prayer
import org.ibadalrahman.prayertimes.presenter.entity.PrayerTimesIntention
import org.ibadalrahman.prayertimes.presenter.entity.PrayerTimesScreenState
import org.ibadalrahman.prayertimes.presenter.entity.WeekDay
import org.ibadalrahman.prayertimes.presenter.entity.WeekPrayerTimesState
import org.ibadalrahman.resources.R
import org.ibadalrahman.fp.safeLet
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
            .padding(horizontal = 20.dp)
            .verticalScroll(ScrollState(0))
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = stringResource(id = R.string.timings).uppercase(),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
        )

        Table(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp)),
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
                        Text(
                            text = stringResource(id = R.string.week),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = FontWeight.Bold,
                            ),
                        )
                    } else if (rowIndex == 0) {
                        Text(
                            text = localizedPrayerName(tableIdxToPrayer(columnIndex)),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = FontWeight.Bold,
                            ),
                        )
                    } else if (columnIndex == 0) {
                        Text(
                            text = localizedWeekDay(tableIdxToWeekDay(rowIndex)),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = FontWeight.Bold,
                            ),
                        )
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

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun Table(
    modifier: Modifier = Modifier,
    rowModifier: Modifier = Modifier,
    horizontalScrollState: ScrollState = rememberScrollState(),
    columnCount: Int,
    rowCount: Int,
    beforeRow: (@Composable (rowIndex: Int) -> Unit)? = null,
    afterRow: (@Composable (rowIndex: Int) -> Unit)? = null,
    cellContent: @Composable (columnIndex: Int, rowIndex: Int) -> Unit
) {
    val columnWidths = remember { mutableStateMapOf<Int, Int>() }
    val density = LocalDensity.current
    val firstColumnWidthDp = with(density) { (columnWidths[0] ?: 0).toDp() }

    Box(modifier = modifier) {
        // Fixed column
        Column(
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            for (rowIndex in 0..rowCount) {
                Column {
                    beforeRow?.invoke(rowIndex)
                    Row(modifier = rowModifier) {
                        Box(modifier = Modifier.layout { measurable, constraints ->
                            val placeable = measurable.measure(constraints)
                            val maxWidth = maxOf(columnWidths[0] ?: 0, placeable.width)
                            columnWidths[0] = maxWidth
                            layout(maxWidth, placeable.height) {
                                placeable.placeRelative(0, 0)
                            }
                        }) {
                            cellContent(0, rowIndex)
                        }
                    }
                    afterRow?.invoke(rowIndex)
                }
            }
        }

        // Scrollable columns
        Box(
            modifier = Modifier
                .padding(start = firstColumnWidthDp)
                .horizontalScroll(horizontalScrollState)
        ) {
            Column {
                for (rowIndex in 0..rowCount) {
                    Column {
                        beforeRow?.invoke(rowIndex)
                        Row(modifier = rowModifier) {
                            (1 until columnCount).forEach { columnIndex ->
                                Box(modifier = Modifier.layout { measurable, constraints ->
                                    val placeable = measurable.measure(constraints)
                                    val maxWidth = maxOf(columnWidths[columnIndex] ?: 0, placeable.width)
                                    columnWidths[columnIndex] = maxWidth
                                    layout(maxWidth, placeable.height) {
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
