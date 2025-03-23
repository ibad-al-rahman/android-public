package com.ibadalrahman.prayertimes.repository.data.remote

import com.ibadalrahman.prayertimes.repository.data.entity.YearDailyPrayerTimesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PrayerTimesApi {
    companion object {
        const val PRAYER_TIMES_ENDPOINT = "v1/year"
    }

    @GET("$PRAYER_TIMES_ENDPOINT/{year}.json")
    fun getYearDailyPrayerTimes(@Path("year") year: Int): Call<YearDailyPrayerTimesResponse>
}
