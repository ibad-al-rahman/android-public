package org.ibadalrahman.publicsector.main.model

import Prayer
import android.content.Context
import android.os.Environment
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
import kotlinx.serialization.InternalSerializationApi
import java.io.File
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

private const val BASE_URL =
    "https://ibad-al-rahman.github.io/prayer-times"

class PrayerRepositoryImpl : PrayerRepository {

    @OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
    override suspend fun getPrayersForDay(day: String): PrayerData {

        var dayData =  getDataForDay(day)

        val prayersForDay = listOf(
            Prayer(name = "Fajr", icon = Icons.Default.Nightlight, time = dayData.prayerTimes.fajr),
            Prayer(name = "Sunrise", icon = Icons.Default.WbTwilight, time = dayData.prayerTimes.sunrise),
            Prayer(name = "Dhuhr", icon = Icons.Default.WbSunny, time = dayData.prayerTimes.dhuhr),
            Prayer(name = "Asr", icon = Icons.Default.WbSunny, time = dayData.prayerTimes.asr),
            Prayer(name = "Maghrib", icon = Icons.Default.WbTwilight, time = dayData.prayerTimes.maghrib),
            Prayer(name = "Ishaa", icon = Icons.Default.Nightlight, time =dayData.prayerTimes.ishaa),
        )

        val hadith = getHadithOfWeek(weekId = dayData.weekId!!, day = day)

        return PrayerData(
            id = dayData.id,
            gregorian = dayData.gregorian,
            hijri = dayData.hijri,
            prayerTimes = prayersForDay,
            eventEn = dayData.event?.en ?: "",
            eventAr = dayData.event?.ar ?: "",
            weekId = dayData.weekId,
            hadith = hadith.hadith,
            note = hadith.note ?: ""

        )

    }

    @OptIn(InternalSerializationApi::class)
    override suspend fun getPrayersForWeek(week: Int): WeekData {

        var currentYear: Int = Calendar.getInstance().get(Calendar.YEAR)
        val yearPrayerData = getDataForYear(currentYear)

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

        var res : List<PrayerDataSerializable> = listOf()

        currentWeek.forEach { dayStr ->
            var prayerData: PrayerDataSerializable? = yearPrayerData.year.find { "" + it.id == dayStr }
            if(prayerData == null) {
                res = res.plus(PrayerDataSerializable(
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




        val weekPrayers =  res.map { prayerData -> listOf(
            prayerData.prayerTimes.fajr,
            prayerData.prayerTimes.sunrise,
            prayerData.prayerTimes.dhuhr,
            prayerData.prayerTimes.asr,
            prayerData.prayerTimes.maghrib,
            prayerData.prayerTimes.ishaa,
        ) }

//        NB: the param `week` passed to this function is the week number without year
//        so to get the corresponding weekId:
        val weekId = "$currentYear$week".toInt()
        val hadith = getHadithOfWeek(weekId = weekId, dayYmd = currentWeek[0]).hadith

        return WeekData(
            prayers = weekPrayers,
            hadith = hadith
        )
    }

    @OptIn(InternalSerializationApi::class)
    suspend fun getDataForYear(year: Int): YearPrayerData {

        val file = File(IOUtils.filesDir ?: File(""), "ibad_$year.json")

        if(file.exists()) {
//            if file exists already, read from the file
            println("READING DATA FROM STORAGE...")
            return Json.decodeFromString<YearPrayerData>(file.readText())
        }

//        otherwise, fetch from API
        println("FETCHING DATA FORM API...")
        println(BASE_URL + "/v1/year/days/$year.json")
        val client = HttpClient(CIO)
        val response: HttpResponse = client.get(BASE_URL + "/v1/year/days/$year.json")

//        write response body to JSON file
        if(file.createNewFile()) {
            file.writeText(response.body())
        }

//        return object to caller
        return Json.decodeFromString<YearPrayerData>(response.body())

    }

    @OptIn(InternalSerializationApi::class)
    suspend fun getDataForDay(day: String): PrayerDataSerializable {
        var dayObj = SimpleDateFormat("dd/MM/yyyy", java.util.Locale.ROOT).parse(day)
        var yearObj = Calendar.getInstance()
        if (dayObj != null) {
            yearObj.time = dayObj
        }
        val year = yearObj.get(Calendar.YEAR)



        val yearPrayerData : YearPrayerData = getDataForYear(year)

        var dayId = SimpleDateFormat("yyyyMMdd", java.util.Locale.ROOT).format(dayObj)

        var dayData = yearPrayerData.year.find { "" + it.id == dayId }

        return dayData ?: PrayerDataSerializable() // equivalent to:  return if(dayData != null) dayData else PrayerDataSerializable()
    }



    @OptIn(InternalSerializationApi::class)
    suspend fun getHadithOfWeek(weekId: Int, day: String = "", dayYmd: String = ""): HadithSerializable {
        var dayObj = Date()

        if(day != "") {
            var dayObj = SimpleDateFormat("dd/MM/yyyy", java.util.Locale.ROOT).parse(day)
        }
        else {
            assert(dayYmd != "")
            var dayObj = SimpleDateFormat("yyyyMMdd", java.util.Locale.ROOT).parse(dayYmd)
        }

        var yearObj = Calendar.getInstance()
        if (dayObj != null) {
            yearObj.time = dayObj
        }
        val year = yearObj.get(Calendar.YEAR)

        val file = File(IOUtils.filesDir ?: File(""), "ibad_weeks_$year.json")

        if(file.exists()) {
//            if file exists already, read from the file
            println("READING WEEKS DATA FROM STORAGE...")
            val yearWeekData =  Json.decodeFromString<YearWeekDataSerializable>(file.readText())
            val weekData = yearWeekData.weeks.find { it.id == weekId }
            return weekData?.hadith ?: HadithSerializable()
        }

//        otherwise, fetch from API
        println("FETCHING DATA FROM WEEKS API...")
        println(BASE_URL + "/v1/year/weeks/$year.json")
        val client = HttpClient(CIO)
        val response: HttpResponse = client.get(BASE_URL + "/v1/year/weeks/$year.json")

//        write response body to JSON file
        if(file.createNewFile()) {
            file.writeText(response.body())
        }

//        return object to caller
        val yearWeekData =  Json.decodeFromString<YearWeekDataSerializable>(response.body())
        val weekData = yearWeekData.weeks.find { it.id == weekId }
        return weekData?.hadith ?: HadithSerializable()

    }
}

@kotlinx.serialization.InternalSerializationApi
@Serializable
data class PrayerDataSerializable(
    val id: Int = 0,
    val gregorian: String = "",
    val hijri: String = "",
    val prayerTimes: DayPrayers = DayPrayers(),
    val event: Event? = null,
    val weekId: Int? = null
)

@kotlinx.serialization.InternalSerializationApi
@Serializable
data class YearPrayerData(
    val year: List<PrayerDataSerializable>,
    val sha1: String
)

@kotlinx.serialization.InternalSerializationApi
@Serializable
data class DayPrayers(
    val fajr: String = "",
    val sunrise: String = "",
    val dhuhr: String = "",
    val asr: String = "",
    val maghrib: String = "",
    val ishaa: String = ""
)

@kotlinx.serialization.InternalSerializationApi
@Serializable
data class Event(
    val ar: String,
    val en: String? = null
)

@kotlinx.serialization.InternalSerializationApi
@Serializable
data class YearWeekDataSerializable (
    val weeks: List<WeekDataSerializable>,
    val sha1: String = ""
)

@kotlinx.serialization.InternalSerializationApi
@Serializable
data class WeekDataSerializable (
    val id: Int,
    val mon: PrayerDataSerializable? = PrayerDataSerializable(),
    val tue: PrayerDataSerializable? = PrayerDataSerializable(),
    val wed: PrayerDataSerializable? = PrayerDataSerializable(),
    val thu: PrayerDataSerializable? = PrayerDataSerializable(),
    val fri: PrayerDataSerializable? = PrayerDataSerializable(),
    val sat: PrayerDataSerializable? = PrayerDataSerializable(),
    val sun: PrayerDataSerializable? = PrayerDataSerializable(),
    val hadith: HadithSerializable = HadithSerializable()
)

@kotlinx.serialization.InternalSerializationApi
@Serializable
data class HadithSerializable (
    val hadith: String = "",
    val note: String? = ""
)

// a non "@serializable" version of PrayerDataSerializable
data class PrayerData(
    val id: Int = 0,
    val gregorian: String = "",
    val hijri: String = "",
    val prayerTimes: List<Prayer> = listOf(),
    val eventEn: String = "",
    val eventAr: String = "",
    val weekId: Int? = null,
    val hadith: String = "",
    val note: String = ""
)

data class WeekData (
    val prayers: List<List<String>> = listOf(),
    val hadith: String = ""
)
