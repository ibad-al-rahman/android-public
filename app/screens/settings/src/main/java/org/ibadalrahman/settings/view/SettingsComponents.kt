package org.ibadalrahman.settings.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.ibadalrahman.resources.R

/** Shared container color for every settings list row. */
internal val listItemColors: ListItemColors
    @Composable get() =
        ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.background)

/** A row that opens an external link — trailing "open in new" glyph. */
@Composable
internal fun OpenLinkButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = { SettingsRowTitle(text) },
        leadingContent = {
            Icon(imageVector = icon, contentDescription = text)
        },
        trailingContent = {
            Icon(
                imageVector = Icons.AutoMirrored.Default.OpenInNew,
                contentDescription = stringResource(R.string.go)
            )
        },
        colors = listItemColors,
        modifier = modifier.clickable(onClick = onClick),
    )
}

/** A row that pushes to another settings screen — trailing chevron, with an optional badge. */
@Composable
internal fun NavigationRow(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badge: String? = null,
) {
    ListItem(
        headlineContent = { SettingsRowTitle(text) },
        leadingContent = {
            Icon(imageVector = icon, contentDescription = text)
        },
        trailingContent = {
            if (badge.isNullOrBlank()) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = stringResource(R.string.go)
                )
            } else {
                Text(
                    text = badge,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        },
        colors = listItemColors,
        modifier = modifier.clickable(onClick = onClick),
    )
}

@Composable
internal fun SettingsRowTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.Medium
        ),
    )
}

/** An uppercased section header, matching the existing "DISPLAY" style. */
@Composable
internal fun SectionHeader(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
    )
}
