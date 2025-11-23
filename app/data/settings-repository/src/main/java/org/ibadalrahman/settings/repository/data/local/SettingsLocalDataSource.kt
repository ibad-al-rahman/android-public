package org.ibadalrahman.settings.repository.data.local

import org.ibadalrahman.settings.repository.data.domain.Theme

interface SettingsLocalDataSource {
    fun saveTheme(theme: Theme)
    fun getTheme(): Theme?
}
