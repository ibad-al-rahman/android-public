package org.ibadalrahman.settings.repository.data.local

import android.content.SharedPreferences
import androidx.core.content.edit
import org.ibadalrahman.settings.repository.data.domain.NotificationSettings
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

    override fun saveNotificationSettings(settings: NotificationSettings) {
        sharedPreferences.edit {
            putBoolean(KEY_ENABLED, settings.enabled)
            putBoolean(KEY_FAJR, settings.fajr)
            putBoolean(KEY_DHUHR, settings.dhuhr)
            putBoolean(KEY_ASR, settings.asr)
            putBoolean(KEY_MAGHRIB, settings.maghrib)
            putBoolean(KEY_ISHAA, settings.ishaa)
            putBoolean(KEY_MORNING_ADHKAR, settings.morningAdhkarEnabled)
            putBoolean(KEY_EVENING_ADHKAR, settings.eveningAdhkarEnabled)
            putInt(KEY_MORNING_HOUR, settings.morningHour)
            putInt(KEY_MORNING_MINUTE, settings.morningMinute)
            putInt(KEY_EVENING_HOUR, settings.eveningHour)
            putInt(KEY_EVENING_MINUTE, settings.eveningMinute)
        }
    }

    override fun getNotificationSettings(): NotificationSettings {
        val default = NotificationSettings.Default
        return NotificationSettings(
            enabled = sharedPreferences.getBoolean(KEY_ENABLED, default.enabled),
            fajr = sharedPreferences.getBoolean(KEY_FAJR, default.fajr),
            dhuhr = sharedPreferences.getBoolean(KEY_DHUHR, default.dhuhr),
            asr = sharedPreferences.getBoolean(KEY_ASR, default.asr),
            maghrib = sharedPreferences.getBoolean(KEY_MAGHRIB, default.maghrib),
            ishaa = sharedPreferences.getBoolean(KEY_ISHAA, default.ishaa),
            morningAdhkarEnabled =
                sharedPreferences.getBoolean(KEY_MORNING_ADHKAR, default.morningAdhkarEnabled),
            eveningAdhkarEnabled =
                sharedPreferences.getBoolean(KEY_EVENING_ADHKAR, default.eveningAdhkarEnabled),
            morningHour = sharedPreferences.getInt(KEY_MORNING_HOUR, default.morningHour),
            morningMinute = sharedPreferences.getInt(KEY_MORNING_MINUTE, default.morningMinute),
            eveningHour = sharedPreferences.getInt(KEY_EVENING_HOUR, default.eveningHour),
            eveningMinute = sharedPreferences.getInt(KEY_EVENING_MINUTE, default.eveningMinute),
        )
    }

    private companion object {
        const val KEY_ENABLED = "notif_enabled"
        const val KEY_FAJR = "notif_fajr"
        const val KEY_DHUHR = "notif_dhuhr"
        const val KEY_ASR = "notif_asr"
        const val KEY_MAGHRIB = "notif_maghrib"
        const val KEY_ISHAA = "notif_ishaa"
        const val KEY_MORNING_ADHKAR = "notif_morning_adhkar"
        const val KEY_EVENING_ADHKAR = "notif_evening_adhkar"
        const val KEY_MORNING_HOUR = "notif_morning_hour"
        const val KEY_MORNING_MINUTE = "notif_morning_minute"
        const val KEY_EVENING_HOUR = "notif_evening_hour"
        const val KEY_EVENING_MINUTE = "notif_evening_minute"
    }
}
