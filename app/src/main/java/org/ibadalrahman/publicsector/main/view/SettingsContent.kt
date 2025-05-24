package org.ibadalrahman.publicsector.main.view

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.os.LocaleListCompat
import org.ibadalrahman.publicsector.R
import androidx.appcompat.app.AppCompatDelegate;
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.outlined.ArrowRightAlt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.os.ConfigurationCompat
import androidx.core.text.layoutDirection
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun SettingsContent() {

    val uriHandler = LocalUriHandler.current
    var context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .padding(20.dp)
    ) {
        Text(
            text = stringResource(id = R.string.settings),
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 40.dp)
        )

        ListButton(
            text = stringResource(R.string.donate),
            icon = Icons.Default.Favorite,
            onClick = {
//                TODO
                uriHandler.openUri("https://www.ibad.org.lb/index.php/home/donationdoors")
            }
        )

        Spacer(
            modifier = Modifier.height(40.dp)
        )
        Text(
            text = stringResource(R.string.display),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(vertical = 10.dp)
        )
        ListButton(
            text = stringResource(R.string.appearance),
            icon = Icons.Default.Contrast,
            onClick = {
//                TODO
            }
        )
        HorizontalDivider()
        LanguageSelector()

        Spacer(
            modifier = Modifier.height(40.dp)
        )

//        todo probably rename to "Visit Website"
        ListButton(
            text = stringResource(R.string.visit_website),
            icon = Icons.Default.Language,
            onClick = {
                uriHandler.openUri("https://www.ibad.org.lb")
            }
        )

        Spacer(
            modifier = Modifier.height(40.dp)
        )

//        TODO do we really need this button?
        ListButton(
            text = stringResource(R.string.clear_cache),
            icon = Icons.Default.CleaningServices,
            onClick = {
                context.cacheDir.deleteRecursively()
            }
        )
    }
}

@Composable
fun ListButton(text: String, icon: ImageVector, onClick : () -> Unit ) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Icon(icon, text)
            Text(text, fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(horizontal = 10.dp))
            Spacer(Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background))
            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Go", modifier = Modifier.mirror())
        }
    }
}

@Composable
fun AppearanceSelector() {
    var expanded by remember { mutableStateOf(false) }
    var context = LocalContext.current
    var select = stringResource(id = R.string.select)
    var selectedAppearance by remember { mutableStateOf(select) }

    TextButton(
        onClick = { expanded = !expanded } ,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)

    ) {

        val str_light = stringResource(id = R.string.light)
        val str_dark = stringResource(id = R.string.dark)
        Row(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)) {
            Icon(Icons.Default.Contrast, stringResource(id = R.string.language))
            Text(stringResource(id = R.string.appearance), fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(horizontal = 10.dp))
            Spacer(Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background))
            Box {
                Text(stringResource(id = R.string.select))
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { str_light },
                        onClick = {
                            setDarkMode(false)
                            selectedAppearance = str_light
                        }
                    )
                    DropdownMenuItem(
                        text = { str_dark },
                        onClick = {
                            setDarkMode(true)
                            selectedAppearance = str_dark
                        }
                    )
                }
            }
        }
    }



}

fun setDarkMode(enabled: Boolean) {
    val value = when (enabled) {
        true -> AppCompatDelegate.MODE_NIGHT_YES
        false -> AppCompatDelegate.MODE_NIGHT_NO
    }

    AppCompatDelegate.setDefaultNightMode(value)
}

@Composable
fun LanguageSelector() {
    val select = stringResource(id = R.string.select)
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf(select) }

    TextButton(
        onClick = { expanded = !expanded } ,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)

    ) {
        Row(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)) {
            Icon(Icons.Default.FontDownload, stringResource(id = R.string.language))
            Text(stringResource(id = R.string.language), fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(horizontal = 10.dp))
            Spacer(Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background))
            Box {
                Text(stringResource(id = R.string.select))
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("English") },
                        onClick = {
                            changeLocales(context, "en")
                            selectedLanguage = "English"
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("العربية") },
                        onClick = {
                            changeLocales(context, "ar")
                            selectedLanguage = "العربية"
                        }
                    )
                }
            }
        }
    }
}


fun changeLocales(context: Context, localeString: String) {
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.getSystemService(LocaleManager::class.java)
            .applicationLocales = LocaleList.forLanguageTags(localeString)
    }
    else {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(localeString))
    }
}

@Composable
@ReadOnlyComposable
fun getLocale(): Locale {
    val configuration = LocalConfiguration.current
    return ConfigurationCompat.getLocales(configuration).get(0) ?: LocaleListCompat.getDefault()[0]!!
}

@Stable
fun Modifier.mirror(): Modifier {
    return if (Locale.getDefault().layoutDirection == LayoutDirection.Rtl.ordinal)
        this.scale(scaleX = -1f, scaleY = 1f)
    else
        this
}
