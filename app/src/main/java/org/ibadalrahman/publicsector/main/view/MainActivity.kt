package org.ibadalrahman.publicsector.main.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import dagger.hilt.android.AndroidEntryPoint
import org.ibadalrahman.publicsector.main.presenter.MainActivityViewModel
import org.ibadalrahman.publicsector.ui.theme.AppTheme
import org.ibadalrahman.settings.repository.SettingsRepository
import org.ibadalrahman.settings.repository.data.domain.Theme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {

    @Inject lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val theme = settingsRepository.getTheme() ?: Theme.System
            val darkTheme = when(theme) {
                Theme.System -> isSystemInDarkTheme()
                Theme.Light -> false
                Theme.Dark -> true
            }
            AppCompatDelegate.setDefaultNightMode(theme.code)

            AppTheme(darkTheme = darkTheme) {
                RootContent()
            }
        }
    }
}
