package com.ibadalrahman.prayertimes.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ibadalrahman.mvi.BaseScreen
import com.ibadalrahman.mvi.ObserveLifecycleEvents
import com.ibadalrahman.prayertimes.presenter.PrayerTimesViewModel
import com.ibadalrahman.prayertimes.presenter.entity.PrayerTimesIntention

@Composable
fun PrayerTimesRootScreen(viewModel: PrayerTimesViewModel) {
    BaseScreen(
        viewModel = viewModel,
        viewActionProcessor = { _ -> }
    ) { state, intentionProcessor ->
        ObserveLifecycleEvents (
            onStart = { intentionProcessor(PrayerTimesIntention.OnScreenStarted) }
        )
        Text("Hello World")
    }
}
