package org.ibadalrahman.settings.calculationmethod.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.ibadalrahman.settings.view.listItemColors

private const val MIN_ANGLE = 10.0
private const val MAX_ANGLE = 29.5
private const val ANGLE_STEP = 0.5

/** A −/+ stepper over a twilight angle in 0.5° steps, clamped to [MIN_ANGLE]..[MAX_ANGLE]. */
@Composable
internal fun AngleRow(
    label: String,
    angle: Double,
    enabled: Boolean,
    onAngleChange: (Double) -> Unit,
) {
    ListItem(
        headlineContent = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Medium,
                ),
            )
        },
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { onAngleChange((angle - ANGLE_STEP).coerceAtLeast(MIN_ANGLE)) },
                    enabled = enabled && angle > MIN_ANGLE,
                ) { Icon(Icons.Filled.Remove, contentDescription = "−") }
                Text(
                    text = formatAngle(angle),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                )
                Spacer(Modifier.width(4.dp))
                IconButton(
                    onClick = { onAngleChange((angle + ANGLE_STEP).coerceAtMost(MAX_ANGLE)) },
                    enabled = enabled && angle < MAX_ANGLE,
                ) { Icon(Icons.Filled.Add, contentDescription = "+") }
            }
        },
        colors = listItemColors,
    )
}
