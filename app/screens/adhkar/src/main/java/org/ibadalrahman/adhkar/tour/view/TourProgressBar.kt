package org.ibadalrahman.adhkar.tour.view

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A thin leading-aligned progress bar. [progress] is clamped to `0..1`. Uses
 * [LinearProgressIndicator], which flips automatically under right-to-left layout. Ports the iOS
 * `TourProgressBar`.
 */
@Composable
fun TourProgressBar(progress: Float, height: Dp = 4.dp) {
    val animated by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        label = "tourProgress",
    )
    LinearProgressIndicator(
        progress = { animated },
        trackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
    )
}
