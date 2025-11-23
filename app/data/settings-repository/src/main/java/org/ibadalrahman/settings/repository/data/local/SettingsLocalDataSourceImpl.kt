package org.ibadalrahman.settings.repository.data.local

import android.content.SharedPreferences
import androidx.core.content.edit
import org.ibadalrahman.settings.repository.data.domain.Theme
import javax.inject.Inject

class SettingsLocalDataSourceImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
): SettingsLocalDataSource {

    override fun saveTheme(theme: Theme) {
        sharedPreferences.edit { putInt("theme", theme.code) }
    }

    override fun getTheme(): Theme? {
        if (!sharedPreferences.contains("theme")) {
            return null
        }

        val code = sharedPreferences.getInt("theme", Theme.System.code)
        return Theme.from(code)
    }
}
