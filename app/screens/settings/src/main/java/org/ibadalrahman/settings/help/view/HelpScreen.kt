package org.ibadalrahman.settings.help.view

import android.content.pm.PackageInfo
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.ibadalrahman.resources.R
import org.ibadalrahman.settings.view.OpenLinkButton
import org.ibadalrahman.settings.view.SectionHeader
import org.ibadalrahman.settings.view.listItemColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(
    onBack: () -> Unit,
    onContactUs: () -> Unit,
) {
    val context = LocalContext.current
    val appVersion = remember {
        val packageInfo: PackageInfo =
            context.packageManager.getPackageInfo(context.packageName, 0)
        val build = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode.toString()
        } else {
            @Suppress("DEPRECATION")
            packageInfo.versionCode.toString()
        }
        AppVersion(name = packageInfo.versionName.orEmpty(), build = build)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.help)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.go),
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            contentPadding = PaddingValues(20.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding())
                .background(MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            item {
                OpenLinkButton(
                    text = stringResource(R.string.contact_us),
                    icon = Icons.Outlined.SupportAgent,
                    onClick = onContactUs,
                    modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                )
            }

            item {
                Spacer(Modifier.height(40.dp))
                SectionHeader(stringResource(R.string.app_info))
                Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))) {
                    InfoRow(stringResource(R.string.version), appVersion.name)
                    Spacer(Modifier.height(1.dp))
                    InfoRow(stringResource(R.string.build_number), appVersion.build)
                }
            }
        }
    }
}

private data class AppVersion(val name: String, val build: String)

@Composable
private fun InfoRow(label: String, value: String) {
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
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
            )
        },
        colors = listItemColors,
    )
}
