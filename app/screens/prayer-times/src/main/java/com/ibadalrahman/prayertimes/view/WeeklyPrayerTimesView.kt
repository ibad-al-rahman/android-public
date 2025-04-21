package com.ibadalrahman.prayertimes.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ibadalrahman.prayertimes.presenter.entity.PrayerTimesIntention
import com.ibadalrahman.prayertimes.presenter.entity.PrayerTimesScreenState

@Composable
fun WeeklyPrayerTimesView(
    state: PrayerTimesScreenState,
    intentionProcessor: (intention: PrayerTimesIntention) -> Unit,
) {
    Text(text = "Hello World")
}
