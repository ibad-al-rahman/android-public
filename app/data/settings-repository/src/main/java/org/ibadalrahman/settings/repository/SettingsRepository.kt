package org.ibadalrahman.settings.repository

import org.ibadalrahman.settings.repository.data.domain.Theme

interface SettingsRepository {
    fun saveTheme(theme: Theme)
    fun getTheme(): Theme?
}
