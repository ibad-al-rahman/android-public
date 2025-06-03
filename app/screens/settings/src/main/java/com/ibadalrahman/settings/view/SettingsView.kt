package com.ibadalrahman.settings.view

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.OpenInNewOff
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.sp
import com.ibadalrahman.resources.R
import com.ibadalrahman.settings.presenter.entity.Language
import com.ibadalrahman.settings.presenter.entity.SettingsIntention

@Composable
fun SettingsView(
    intentionProcessor: (intention: SettingsIntention) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .padding(20.dp)
    ) {
        OpenLinkButton(
            text = stringResource(R.string.donate),
            icon = Icons.Outlined.FavoriteBorder,
            onClick = {
                intentionProcessor(SettingsIntention.Donate)
            }
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = stringResource(id = R.string.display).uppercase(),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
        )

        Column(modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
        ) {
            LanguageSelector(intentionProcessor = intentionProcessor)
//            HorizontalDivider()
//            ThemeSelector(intentionProcessor = intentionProcessor)
        }

        Spacer(modifier = Modifier.height(40.dp))

        OpenLinkButton(
            text = stringResource(R.string.contact_us),
            icon = Icons.Outlined.SupportAgent,
            onClick = {
                intentionProcessor(SettingsIntention.ContactUs)
            }
        )

        Spacer(modifier = Modifier.height(40.dp))

        ListButton(
            text = stringResource(R.string.clear_cache),
            onClick = { intentionProcessor(SettingsIntention.ClearCache) }
        )
    }
}

@Composable
fun ListButton(text: String, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 10.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
            )
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
            )
        }
    }
}

@Composable
fun OpenLinkButton(text: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .clickable { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier.padding(end = 16.dp),
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Medium
            ),
        )
        Spacer(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        )
        Icon(imageVector = Icons.AutoMirrored.Default.OpenInNew, contentDescription = "Go")
    }
}

@Composable
fun LanguageSelector(intentionProcessor: (intention: SettingsIntention) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .clickable { expanded = !expanded }
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
        ) {
            Icon(
                imageVector = Icons.Default.FontDownload,
                contentDescription = stringResource(id = R.string.language),
                modifier = Modifier.padding(end = 16.dp),
            )
            Text(
                text = stringResource(id = R.string.language),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Medium
                ),
            )
            Spacer(modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
            )
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
        }
    }
}

@Composable
fun ThemeSelector(intentionProcessor: (intention: SettingsIntention) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .clickable { expanded = !expanded }
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
        ) {
            Icon(
                imageVector = Icons.Default.Contrast,
                contentDescription = stringResource(id = R.string.language),
                modifier = Modifier.padding(end = 16.dp),
            )
            Text(
                text = stringResource(id = R.string.appearance),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Medium
                ),
            )
            Spacer(modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
            )
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
                        text = { Text("Dark") },
                        onClick = {
                            intentionProcessor(
                                SettingsIntention.ChangeTheme(AppCompatDelegate.MODE_NIGHT_YES)
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Light") },
                        onClick = {
                            intentionProcessor(
                                SettingsIntention.ChangeTheme(AppCompatDelegate.MODE_NIGHT_NO)
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("System") },
                        onClick = {
                            intentionProcessor(
                                SettingsIntention.ChangeTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                            )
                        }
                    )
                }
            }
        }
    }
}
