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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.ibadalrahman.resources.R
import org.ibadalrahman.settings.presenter.entity.Language
import org.ibadalrahman.settings.presenter.entity.SettingsIntention

@Composable
fun SettingsView(
    intentionProcessor: (intention: SettingsIntention) -> Unit,
    openNotifications: () -> Unit,
    openPrayerTimesCalculation: () -> Unit,
    openAppearance: () -> Unit,
    openHelp: () -> Unit,
    openRateUs: () -> Unit,
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
        // Donate
        item {
            OpenLinkButton(
                text = stringResource(R.string.donate),
                icon = Icons.Outlined.FavoriteBorder,
                onClick = { intentionProcessor(SettingsIntention.Donate) },
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            )
        }

        // Notifications
        item {
            Spacer(modifier = Modifier.height(40.dp))
            NavigationRow(
                text = stringResource(R.string.notifications),
                icon = Icons.Filled.Notifications,
                onClick = openNotifications,
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            )
        }

        // Prayer Times
        item {
            Spacer(modifier = Modifier.height(40.dp))
            SectionHeader(stringResource(R.string.prayer_times))
            NavigationRow(
                text = stringResource(R.string.prayer_times_calculation_method),
                icon = Icons.Filled.Tune,
                onClick = openPrayerTimesCalculation,
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            )
        }

        // Display
        item {
            Spacer(modifier = Modifier.height(40.dp))
            SectionHeader(stringResource(R.string.display))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            ) {
                LanguageSelector(intentionProcessor = intentionProcessor)
                Spacer(Modifier.height(1.dp))
                NavigationRow(
                    text = stringResource(R.string.appearance),
                    icon = Icons.Filled.Contrast,
                    onClick = openAppearance,
                )
            }
        }

        // Help & Social
        item {
            Spacer(modifier = Modifier.height(40.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            ) {
                NavigationRow(
                    text = stringResource(R.string.help),
                    icon = Icons.Outlined.HelpOutline,
                    onClick = openHelp,
                )
                Spacer(Modifier.height(1.dp))
                OpenLinkButton(
                    text = stringResource(R.string.rate_us),
                    icon = Icons.Filled.StarBorder,
                    onClick = openRateUs,
                )
                Spacer(Modifier.height(1.dp))
                OpenLinkButton(
                    text = stringResource(R.string.invite_friends),
                    icon = Icons.Filled.Share,
                    onClick = { intentionProcessor(SettingsIntention.ShareApp) },
                )
            }
        }
    }
}

@Composable
private fun LanguageSelector(intentionProcessor: (intention: SettingsIntention) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Row(modifier = Modifier.fillMaxWidth()) {
        ListItem(
            headlineContent = { SettingsRowTitle(stringResource(id = R.string.language)) },
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
                                expanded = false
                                intentionProcessor(SettingsIntention.ChangeLanguage(Language.En))
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("العربية") },
                            onClick = {
                                expanded = false
                                intentionProcessor(SettingsIntention.ChangeLanguage(Language.Ar))
                            }
                        )
                    }
                }
            },
            colors = listItemColors,
            modifier = Modifier.clickable(onClick = { expanded = !expanded }),
        )
    }
}
