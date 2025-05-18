package com.ibadalrahman.prayertimes.presenter.entity

sealed interface PrayerTimesViewAction {
    data class Share(val text: String): PrayerTimesViewAction
}
