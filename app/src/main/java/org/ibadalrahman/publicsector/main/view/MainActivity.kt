package org.ibadalrahman.publicsector.main.view

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dagger.hilt.android.AndroidEntryPoint
import org.ibadalrahman.publicsector.ui.theme.AppTheme
import org.ibadalrahman.settings.repository.SettingsRepository
import org.ibadalrahman.settings.repository.data.domain.Theme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {

    @Inject lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val theme by settingsRepository.themeFlow.collectAsState()
            val darkTheme = when(theme) {
                Theme.System -> isSystemInDarkTheme()
                Theme.Light -> false
                Theme.Dark -> true
            }

            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.auto(
                    lightScrim = Color.TRANSPARENT,
                    darkScrim = Color.TRANSPARENT,
                    detectDarkMode = { darkTheme }
                )
            )

            AppTheme(darkTheme = darkTheme) {
                RootContent()
            }
        }
    }
}
