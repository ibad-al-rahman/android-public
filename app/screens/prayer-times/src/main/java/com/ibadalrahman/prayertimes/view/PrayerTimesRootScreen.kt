package com.ibadalrahman.prayertimes.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ibadalrahman.mvi.BaseScreen
import com.ibadalrahman.mvi.ObserveLifecycleEvents
import com.ibadalrahman.prayertimes.presenter.PrayerTimesViewModel
import com.ibadalrahman.prayertimes.presenter.entity.PrayerTimesIntention
import com.ibadalrahman.prayertimes.presenter.entity.PrayerTimesViewAction
import com.ibadalrahman.prayertimes.presenter.entity.PrayerViewType
import com.ibadalrahman.resources.R

@Composable
fun PrayerTimesRootScreen(
    viewModel: PrayerTimesViewModel,
    onShare: (String) -> Unit,
) {
    BaseScreen(
        viewModel = viewModel,
        viewActionProcessor = { viewAction ->
            when (viewAction) {
                is PrayerTimesViewAction.Share -> {
                    onShare(viewAction.text)
                }
            }
        }
    ) { state, intentionProcessor ->
        ObserveLifecycleEvents (
            onStart = { intentionProcessor(PrayerTimesIntention.OnScreenStarted) }
        )

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            TabRow(
                selectedTabIndex = state.prayerViewType.ordinal,
            ) {
                Tab(
                    selected = state.prayerViewType == PrayerViewType.DAILY,
                    onClick = {
                        intentionProcessor(PrayerTimesIntention.OnTapDailyView)
                    },
                    text = {
                        Text(text = stringResource(id = R.string.daily), fontSize = 18.sp)
                    }
                )

                Tab(
                    selected = state.prayerViewType == PrayerViewType.WEEKLY,
                    onClick = {
                        intentionProcessor(PrayerTimesIntention.OnTapWeeklyView)
                    },
                    text = {
                        Text(text = stringResource(id = R.string.weekly), fontSize = 18.sp)
                    }
                )
            }

            if (state.hasError) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.error_loading_prayer_times),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { intentionProcessor(PrayerTimesIntention.OnScreenStarted) },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(text = stringResource(id = R.string.retry))
                    }
                }
            } else {
                when (state.prayerViewType) {
                    PrayerViewType.DAILY ->
                        DailyPrayerTimesView(state = state, intentionProcessor = intentionProcessor)
                    PrayerViewType.WEEKLY ->
                        WeeklyPrayerTimesView(state = state, intentionProcessor = intentionProcessor)
                }
            }
        }
    }
}
