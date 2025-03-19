package org.ibadalrahman.publicsector.main.model

import Prayer
import java.util.Date

interface PrayerRepository {
     suspend fun getPrayersForDay(day: String) : List<Prayer>
     suspend fun getPrayersForWeek(week: Int) : List<List<String>>
}
