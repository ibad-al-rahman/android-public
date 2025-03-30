package org.ibadalrahman.publicsector.main.model

import Prayer

enum class PrayerPage {
    DAILY,
    WEEKLY
}

data class ViewState (
    val prayerData: PrayerData  = PrayerData(),
    val prayersWeek: List<List<String>>  = listOf(),
    val inputDate: String = "",
    val nearestPrayerIndex: Int = -1,
    val showDatePicker: Boolean = false,
    val currentPrayerPage: PrayerPage = PrayerPage.DAILY,
    val isLoading: Boolean = false
)
