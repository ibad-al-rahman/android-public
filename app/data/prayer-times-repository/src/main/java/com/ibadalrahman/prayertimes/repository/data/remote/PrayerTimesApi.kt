package com.ibadalrahman.prayertimes.repository.data.remote

import com.ibadalrahman.prayertimes.repository.data.remote.responses.Sha1Response
import com.ibadalrahman.prayertimes.repository.data.remote.responses.YearDailyPrayerTimesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PrayerTimesApi {
    companion object {
        const val PRAYER_TIMES_ENDPOINT = "v1/year/days"
        const val SHA1_ENDPOINT = "v1/sha1"
    }

    @GET("$PRAYER_TIMES_ENDPOINT/{year}.json")
    fun getYearDailyPrayerTimes(@Path("year") year: Int): Call<YearDailyPrayerTimesResponse>

    @GET("$SHA1_ENDPOINT/{year}.json")
    fun getYearSha1(@Path("year") year: Int): Call<Sha1Response>
}
