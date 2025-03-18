package org.ibadalrahman.publicsector.main.model

import Prayer

enum class PrayerPage {
    DAILY,
    WEEKLY
}

data class ViewState (
    val prayersDay: Array<Prayer>  = arrayOf(),
    val prayersWeek: Array<Array<String>>  = arrayOf(),
    val inputDate: String = "",
    val nearestPrayerIndex: Int = -1,
    val showDatePicker: Boolean = false,
    val currentPrayerPage: PrayerPage = PrayerPage.DAILY,
    val isLoading: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ViewState

        if (nearestPrayerIndex != other.nearestPrayerIndex) return false
        if (showDatePicker != other.showDatePicker) return false
        if (isLoading != other.isLoading) return false
        if (!prayersDay.contentEquals(other.prayersDay)) return false
        if (!prayersWeek.contentDeepEquals(other.prayersWeek)) return false
        if (inputDate != other.inputDate) return false
        if (currentPrayerPage != other.currentPrayerPage) return false

        return true
    }

    override fun hashCode(): Int {
        var result = nearestPrayerIndex
        result = 31 * result + showDatePicker.hashCode()
        result = 31 * result + isLoading.hashCode()
        result = 31 * result + prayersDay.contentHashCode()
        result = 31 * result + prayersWeek.contentDeepHashCode()
        result = 31 * result + inputDate.hashCode()
        result = 31 * result + currentPrayerPage.hashCode()
        return result
    }
}
