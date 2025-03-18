package org.ibadalrahman.publicsector.main.presenter

sealed class MainIntention {
    object LoadPrayers : MainIntention()
    data class SetDate(val inputDate: String) : MainIntention()
    object LoadDayView : MainIntention()
    object LoadWeekView : MainIntention()
}
