package com.ibadalrahman.prayertimes.repository.data.remote

import com.ibadalrahman.prayertimes.repository.data.remote.responses.Sha1Response
import com.ibadalrahman.prayertimes.repository.data.remote.responses.YearDailyPrayerTimesResponse
import com.ibadalrahman.prayertimes.repository.data.remote.responses.YearWeeklyPrayerTimesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PrayerTimesApi {
    companion object {
        const val SHA1_ENDPOINT = "v1/sha1"
        const val DAYS_ENDPOINT = "v1/year/days"
        const val WEEKS_ENDPOINT = "v1/year/weeks"
    }

    @GET("$DAYS_ENDPOINT/{year}.json")
    fun getYearDailyPrayerTimes(@Path("year") year: Int): Call<YearDailyPrayerTimesResponse>

    @GET("$WEEKS_ENDPOINT/{year}.json")
    fun getYearWeeklyPrayerTimes(@Path("year") year: Int): Call<YearWeeklyPrayerTimesResponse>

    @GET("$SHA1_ENDPOINT/{year}.json")
    fun getYearSha1(@Path("year") year: Int): Call<Sha1Response>
}
