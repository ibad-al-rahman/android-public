package org.ibadalrahman.settings.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.ibadalrahman.resources.R
import org.ibadalrahman.settings.presenter.entity.Language
import org.ibadalrahman.settings.presenter.entity.SettingsIntention
import org.ibadalrahman.settings.repository.data.domain.Theme

@Composable
fun SettingsView(
    intentionProcessor: (intention: SettingsIntention) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        contentPadding = PaddingValues(20.dp),
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        item {
            OpenLinkButton(
                text = stringResource(R.string.donate),
                icon = Icons.Outlined.FavoriteBorder,
                onClick = {
                    intentionProcessor(SettingsIntention.Donate)
                },
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            )
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = stringResource(id = R.string.display).uppercase(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
            )
        }

        item {
            Column(modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
            ) {
                LanguageSelector(intentionProcessor = intentionProcessor)
                Spacer(Modifier.height(1.dp))
                ThemeSelector(intentionProcessor = intentionProcessor)
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }

        item {
            Column(modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
            ) {
                OpenLinkButton(
                    text = stringResource(R.string.contact_us),
                    icon = Icons.Outlined.SupportAgent,
                    onClick = {
                        intentionProcessor(SettingsIntention.ContactUs)
                    }
                )
                Spacer(modifier = Modifier.height(1.dp))
                OpenLinkButton(
                    text = stringResource(R.string.share_app),
                    icon = Icons.Filled.Share,
                    onClick = { intentionProcessor(SettingsIntention.ShareApp) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))

            ListButton(
                text = stringResource(R.string.clear_cache),
                onClick = { intentionProcessor(SettingsIntention.ClearCache) }
            )
        }
    }
}

@Composable
fun ListButton(text: String, onClick: () -> Unit) {
    ListItem(
        headlineContent = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background,
            headlineColor = MaterialTheme.colorScheme.primary,
        ),
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
    )
}

@Composable
fun OpenLinkButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Medium
                ),
            )
        },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = text,
            )
        },
        trailingContent = {
            Icon(imageVector = Icons.AutoMirrored.Default.OpenInNew, contentDescription = "Go")
        },
        colors = listItemColors,
        modifier = modifier
            .clickable(onClick = onClick),
    )
}

@Composable
fun LanguageSelector(intentionProcessor: (intention: SettingsIntention) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        ListItem(
            headlineContent = {
                Text(
                    text = stringResource(id = R.string.language),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Medium
                    ),
                )
            },
            leadingContent = {
                Icon(
                    imageVector = Icons.Default.FontDownload,
                    contentDescription = stringResource(id = R.string.language),
                )
            },
            trailingContent = {
                Box {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = stringResource(id = R.string.select),
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("English") },
                            onClick = {
                                intentionProcessor(SettingsIntention.ChangeLanguage(Language.En))
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("العربية") },
                            onClick = {
                                intentionProcessor(SettingsIntention.ChangeLanguage(Language.Ar))
                            }
                        )
                    }
                }
            },
            colors = listItemColors,
            modifier = Modifier
                .clickable(onClick = { expanded = !expanded }),
        )
    }
}

@Composable
fun ThemeSelector(intentionProcessor: (intention: SettingsIntention) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = stringResource(id = R.string.appearance),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Medium
                    ),
                )
            },
            leadingContent = {
                Icon(
                    imageVector = Icons.Default.Contrast,
                    contentDescription = stringResource(id = R.string.language),
                )
            },
            trailingContent = {
                Box {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = stringResource(id = R.string.select),
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.dark)) },
                            onClick = {
                                expanded = false
                                intentionProcessor(SettingsIntention.ChangeTheme(Theme.Dark))
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.light)) },
                            onClick = {
                                expanded = false
                                intentionProcessor(SettingsIntention.ChangeTheme(Theme.Light))
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.system)) },
                            onClick = {
                                expanded = false
                                intentionProcessor(SettingsIntention.ChangeTheme(Theme.System))
                            }
                        )
                    }
                }
            },
            colors = listItemColors,
            modifier = Modifier
                .clickable { expanded = !expanded },
        )
    }
}

private val listItemColors: ListItemColors
    @Composable get() =
        ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.background)
