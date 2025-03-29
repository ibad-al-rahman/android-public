package com.ibadalrahman.prayertimes.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ibadalrahman.mvi.BaseScreen
import com.ibadalrahman.prayertimes.presenter.PrayerTimesViewModel

@Composable
fun PrayerTimesRootScreen(viewModel: PrayerTimesViewModel) {
    BaseScreen(
        viewModel = viewModel,
        viewActionProcessor = { _ -> }
    ) { state, intentionProcessor ->
        Text("Hello World")
    }
}
