package org.ibadalrahman.adhkar.tour.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * An accent-colored check mark inside a ringed circle. Used both for a completed dhikr's done state
 * and for the tour completion screen; [size] scales it. Ports the iOS `CompletionMark`.
 */
@Composable
fun CompletionMark(size: Dp = 72.dp) {
    val accent = MaterialTheme.colorScheme.primary
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(size)
            .drawBehind {
                val radius = this.size.minDimension / 2f
                val center = Offset(this.size.width / 2f, this.size.height / 2f)
                drawCircle(color = accent.copy(alpha = 0.15f), radius = radius, center = center)
                drawCircle(
                    color = accent,
                    radius = radius,
                    center = center,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = radius * 0.06f),
                )
            },
    ) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = null,
            tint = accent,
            modifier = Modifier.size(size * 0.4f),
        )
    }
}
