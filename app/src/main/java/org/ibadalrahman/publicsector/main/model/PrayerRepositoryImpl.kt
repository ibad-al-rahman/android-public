package org.ibadalrahman.publicsector.main.model

import Prayer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import java.net.URL
import java.text.SimpleDateFormat
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import java.util.Calendar
import java.util.GregorianCalendar

private const val BASE_URL =
    "https://ibad-al-rahman.github.io/prayer-times"

class PrayerRepositoryImpl : PrayerRepository {

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getPrayersForDay(day: String): List<Prayer> {

//       `day` param is formatted as dd/MM/yyyy
//        for the API, we need to convert it to yyyy/MM/dd

        var dayObj = SimpleDateFormat("dd/MM/yyyy", java.util.Locale.ROOT).parse(day)
        var dayYmd = SimpleDateFormat("yyyy/MM/dd", java.util.Locale.ROOT).format(dayObj)

        val client = HttpClient(CIO)
        val response: HttpResponse = client.get(BASE_URL + "/v1/day/$dayYmd.json")

        val prayerData = Json.decodeFromString<PrayerData>(response.body())

        val prayerTimes = prayerData.prayerTimes

        return listOf(
            Prayer(name = "Fajr", icon = Icons.Default.Nightlight, time = prayerTimes.fajr),
            Prayer(name = "Sunrise", icon = Icons.Default.WbTwilight, time = prayerTimes.sunrise),
            Prayer(name = "Dhuhr", icon = Icons.Default.WbSunny, time = prayerTimes.dhuhr),
            Prayer(name = "Asr", icon = Icons.Default.WbSunny, time = prayerTimes.asr),
            Prayer(name = "Maghrib", icon = Icons.Default.WbTwilight, time = prayerTimes.maghrib),
            Prayer(name = "Ishaa", icon = Icons.Default.Nightlight, time =prayerTimes.ishaa),
        )

    }

    override suspend fun getPrayersForWeek(week: Int): List<List<String>> {

        var currentYear: Int = Calendar.getInstance().get(Calendar.YEAR);
        val client = HttpClient(CIO)
        val response: HttpResponse = client.get(BASE_URL + "/v1/year/days/$currentYear.json")

        val yearPrayerData = Json.decodeFromString<YearPrayerData>(response.body())

        var currentWeek: List<String> = listOf()
        var now: Calendar = Calendar.getInstance()
        var sdf_ymd = SimpleDateFormat("yyyyMMdd", java.util.Locale.ROOT)

        var delta = -now.get(GregorianCalendar.DAY_OF_WEEK) // nb: week starts on Saturday. To start on Monday instead, add + 2
        now.add(Calendar.DAY_OF_MONTH, delta );
        for (i in 0..6)
        {
            currentWeek = currentWeek.plus(sdf_ymd.format(now.time))
            now.add(Calendar.DAY_OF_MONTH, 1);
        }

        var res : List<PrayerData> = listOf()

        currentWeek.forEach { dayStr ->
            var prayerData: PrayerData? = yearPrayerData.year.find { "" + it.id == dayStr }
            if(prayerData == null) {
                res = res.plus(PrayerData(
                    id = dayStr.toInt(),
                    gregorian = "",
                    hijri = "",
                    prayerTimes = DayPrayers(
                        fajr = "--",
                        sunrise = "--",
                        dhuhr = "--",
                        asr = "--",
                        maghrib = "--",
                        ishaa = "--",
                    )
                ))
            }
            else {
                res = res.plus(prayerData)
            }
        }




        return res.map { prayerData -> listOf(
            prayerData.prayerTimes.fajr,
            prayerData.prayerTimes.sunrise,
            prayerData.prayerTimes.dhuhr,
            prayerData.prayerTimes.asr,
            prayerData.prayerTimes.maghrib,
            prayerData.prayerTimes.ishaa,
        ) }
    }
}

@Serializable
data class PrayerData(
    val id: Int,
    val gregorian: String,
    val hijri: String,
    val prayerTimes: DayPrayers,
    val event: Event? = null,
    val weekId: Int? = null
)

@Serializable
data class YearPrayerData(
    val year: List<PrayerData>,
    val sha1: String
)

@Serializable
data class DayPrayers(
    val fajr: String,
    val sunrise: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val ishaa: String
)

@Serializable
data class Event(
    val ar: String,
    val en: String? = null
)
