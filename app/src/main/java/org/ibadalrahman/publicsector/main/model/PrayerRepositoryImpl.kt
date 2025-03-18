package org.ibadalrahman.publicsector.main.model

import Prayer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.ui.graphics.vector.ImageVector

class PrayerRepositoryImpl : PrayerRepository {

    override suspend fun getPrayersForDay(day: String): Array<Prayer> {

        return arrayOf(
            Prayer(name = "Fajr", icon = Icons.Default.Nightlight, time = "4:22 AM"),
            Prayer(name = "Sunrise", icon = Icons.Default.WbTwilight, time = "5:52 AM"),
            Prayer(name = "Dhuhr", icon = Icons.Default.WbSunny, time = "11:48 AM"),
            Prayer(name = "Asr", icon = Icons.Default.WbSunny, time = "3:10 PM"),
            Prayer(name = "Maghrib", icon = Icons.Default.WbTwilight, time = "5:48 PM"),
            Prayer(name = "Ishaa", icon = Icons.Default.Nightlight, time = "7:04 PM"),
        )
    }

    override suspend fun getPrayersForWeek(week: Int): Array<Array<String>> {

        return arrayOf(
            arrayOf("Sat", "4:27 ", "5:58 ", "11:49 ", "3:08 ", "5:44 ", "7:01 "),
            arrayOf("Sun", "4:27 ", "5:58 ", "11:49 ", "3:08 ", "5:44 ", "7:01 "),
            arrayOf("Mon", "4:27 ", "5:58 ", "11:49 ", "3:08 ", "5:44 ", "7:01 "),
            arrayOf("Tue", "4:27 ", "5:58 ", "11:49 ", "3:08 ", "5:44 ", "7:01 "),
            arrayOf("Wed", "4:27 ", "5:58 ", "11:49 ", "3:08 ", "5:44 ", "7:01 "),
            arrayOf("Thu", "4:27 ", "5:58 ", "11:49 ", "3:08 ", "5:44 ", "7:01 "),
            arrayOf("Fri", "4:27 ", "5:58 ", "11:49 ", "3:08 ", "5:44 ", "7:01 "),
        )
    }
}
