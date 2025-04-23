package org.ibadalrahman.publicsector.main.view

import Prayer
import android.content.Context
import android.content.Intent
import android.icu.text.DateFormat
import android.provider.CalendarContract.Colors
import android.text.format.DateUtils
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.ConfigurationCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.ibadalrahman.publicsector.R
import org.ibadalrahman.publicsector.main.model.PrayerData
import org.ibadalrahman.publicsector.navigation.DatePickerModal
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.chrono.HijrahChronology
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.abs
@Preview
@Composable
fun PrayerTimesDailyContent(
    inputDate: String = "",
    isLoading: Boolean = false,
    prayerData: PrayerData = PrayerData(),
    onDateSelected: (String) -> Unit = {_ -> null}
) {
    var context = LocalContext.current
    var showDatePicker by remember {
        mutableStateOf(false)
    }
    val sdf = remember { SimpleDateFormat("hh:mm a", java.util.Locale.ROOT)}
    var nearestPrayerIdx = remember {mutableIntStateOf(-1)}

//    get which prayer is the nearest from current time, so it gets highlighted
    var min_diff_time = Long.MAX_VALUE
    var min_idx = -1

    prayerData.prayerTimes.forEachIndexed { index, prayer ->

//        un-highlight all prayers
        prayer.highlight = false

        val prayer_date = sdf.parse(prayer.time)
        val diff_time = computeTimeDiffFromNow(prayer_date)
        if(abs(diff_time) < min_diff_time) {
            min_diff_time = abs(diff_time)
            min_idx = index
        }
    }
    nearestPrayerIdx.value = min_idx
    if(min_idx > 0 && min_idx < prayerData.prayerTimes.size) {
        prayerData.prayerTimes[min_idx].highlight = true
    }


    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .padding(
                top = 30.dp,
                bottom = 0.dp,
                start = 20.dp,
                end = 20.dp
            )
            .verticalScroll(ScrollState(0))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center

        ) {
            Image(
                painterResource(R.drawable.ibad_logo_transparent),
                contentDescription = "",
                modifier = Modifier.width(100.dp))
        }

//        Box(modifier = Modifier
//            .fillMaxWidth()
//        ) {
//            ClockText()
//        }

        Spacer(modifier = Modifier.height(15.dp))

        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 10.dp, vertical = 5.dp)
//                .padding(start = 30.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)

        ){
            Text(
                text = stringResource(id = R.string.date),
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            FilledTonalButton(onClick = { showDatePicker = true }) {
                Text(text = inputDate,
                    fontSize = 16.sp)
            }
            if(showDatePicker) {
                DatePickerModal(
                    onDateSelected =  onDateSelected,
                    onDismiss = {
                        showDatePicker = false;
                    }
                )
            }
        }

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        Row {
            Text(
                text = stringResource(id = R.string.timings).uppercase(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(vertical = 10.dp).weight(1.0F)
            )

            Spacer(Modifier.weight(1.0F, true))
            TextButton(
                modifier = Modifier.weight(1.0F),
                onClick = {
                    sharePrayerTimes(prayerData, context)
                }
            ) {
                Icon(Icons.Default.IosShare, stringResource(id = R.string.share))
                Spacer(Modifier.width(5.dp))
                Text(
                    text = stringResource(id = R.string.share) + " ",
                    textAlign = TextAlign.Right
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            prayerData.prayerTimes.forEachIndexed { idx, prayer ->
               PrayerRow(prayer)
           }
        }

        EventsRow(prayerData)

        Spacer(Modifier.height(30.dp))
        Text(
            text = stringResource(id = R.string.hadith).uppercase(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(vertical = 10.dp)
        )
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = prayerData.hadith,
                textAlign = TextAlign.Right,
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(30.dp))



    }
}

@Composable
fun EventsRow(prayerData: PrayerData) {
    if(prayerData.eventEn != "" && getLocale() == Locale.forLanguageTag("en")) {
        Spacer(
            modifier = Modifier.height(30.dp)
        )
        Text(
            text = stringResource(id = R.string.events).uppercase(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(vertical = 10.dp)
        )
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = prayerData.eventEn,
                textAlign = TextAlign.Start,
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
    else if(prayerData.eventAr != "") {
        Text(
            text = stringResource(id = R.string.events).uppercase(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(vertical = 10.dp)
        )
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = prayerData.eventAr,
                textAlign = TextAlign.Start,
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

fun computeTimeDiffFromNow(inputDate: Date): Long {
    var currentDate = Date()

    var currentTime = Calendar.getInstance(); // locale-specific
    currentTime.set(Calendar.HOUR_OF_DAY, currentDate.hours);
    currentTime.set(Calendar.MINUTE, currentDate.minutes);
    currentTime.set(Calendar.SECOND, currentDate.seconds);

    var inputTime = Calendar.getInstance(); // locale-specific
    inputTime.set(Calendar.HOUR_OF_DAY, inputDate.hours);
    inputTime.set(Calendar.MINUTE, inputDate.minutes);
    inputTime.set(Calendar.SECOND, inputDate.seconds);

    return currentTime.timeInMillis - inputTime.timeInMillis


}

@Composable
fun PrayerRow(prayer: Prayer) {
    var bg = if(prayer.highlight) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.background
    var fw = if(prayer.highlight) FontWeight.Bold else FontWeight.Normal

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .background(bg)
            .padding(horizontal = 15.dp, vertical = 15.dp)
    ) {
        var timeDiffString by remember { mutableStateOf("") }
        var prayerTimeSdf = SimpleDateFormat("hh:mm a",  java.util.Locale.ROOT)

        if(prayer.highlight) {
            LaunchedEffect(key1 = Unit) {
                while(isActive) {
                    timeDiffString = displayTime(computeTimeDiffFromNow(prayerTimeSdf.parse(prayer.time)), showSeconds = true, twentyFourHourFormat = true)
                    delay(1000)
                }
            }
        }
        Icon(imageVector = prayer.icon, prayer.name, modifier = Modifier.padding(horizontal = 10.dp))
        Text(text = getLocalizedPrayerName(prayer.name), fontSize = 16.sp, fontWeight = fw)

        if(prayer.highlight) {
            Text(timeDiffString, color = MaterialTheme.colorScheme.onPrimaryContainer, fontSize = 16.sp, fontWeight = fw, modifier = Modifier.weight(1f).padding(horizontal = 20.dp), textAlign = TextAlign.End)
        }
        else {
            Spacer(Modifier.weight(1f).fillMaxWidth().background(Color.White))
        }

        Text(prayer.time.replace("am", stringResource(R.string.am)).replace("pm", stringResource(R.string.pm)), fontSize = 16.sp, fontWeight = fw)
    }
}

fun displayTime(millis : Long = 0, showSeconds: Boolean = false, twentyFourHourFormat : Boolean = false) : String {
    val hours = if(twentyFourHourFormat) "HH" else "hh"
    val secondsSuffix = if(showSeconds) ":ss" else ""
    val am = if(!twentyFourHourFormat) "a" else ""
    val sdf = SimpleDateFormat("$hours:mm$secondsSuffix $am",  java.util.Locale.ROOT)
    sdf.timeZone = TimeZone.getTimeZone("UTC")

    var prefix = "+ "
    var displayMillis = millis
    if(millis < 0) {
        displayMillis = -millis
        prefix = "- "
    }

    var dateObj = Date()
    dateObj.time = displayMillis

    return prefix + sdf.format(dateObj)
}

@Composable
fun getLocalizedPrayerName(name: String): String {
    when(name) {
        "Fajr" -> return stringResource(R.string.fajr)
        "Sunrise" -> return stringResource(R.string.sunrise)
        "Dhuhr" -> return stringResource(R.string.dhuhr)
        "Asr" -> return stringResource(R.string.asr)
        "Maghrib" -> return stringResource(R.string.maghrib)
        "Ishaa" -> return stringResource(R.string.ishaa)
        else -> return name
    }
}


fun sharePrayerTimes(prayerData: PrayerData, context: Context) {

    val inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val gregorianDate = inputFormatter.parse(prayerData.gregorian)

    val hijriDate = HijrahChronology.INSTANCE.date(inputFormatter.withChronology(HijrahChronology.INSTANCE).parse(prayerData.hijri))


    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy").withLocale(Locale.forLanguageTag("ar-LB"))
    val gregorianDateFormatted = formatter.format(gregorianDate)
    val hijriDateFormatted = formatter.format(hijriDate)

    val text_to_send = """
        مواقيت الصلاة بحسب تقويم جماعة عباد الرحمٰن السنوي
        ${hijriDateFormatted}
        ${gregorianDateFormatted}

        🌌 الفجر: ${prayerData.prayerTimes[0].time.replace("am", "ص").replace("pm", "م")} 🌌

        🌄 الشروق: ${prayerData.prayerTimes[1].time.replace("am", "ص").replace("pm", "م")} 🌄

        ☀ الظهر: ${prayerData.prayerTimes[2].time.replace("am", "ص").replace("pm", "م")} ☀

        🌆 العصر: ${prayerData.prayerTimes[3].time.replace("am", "ص").replace("pm", "م")} 🌆

        🌅 المغرب: ${prayerData.prayerTimes[4].time.replace("am", "ص").replace("pm", "م")} 🌅

        🌃 العشاء: ${prayerData.prayerTimes[5].time.replace("am", "ص").replace("pm", "م")} 🌃

    """.trimIndent()

    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text_to_send)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)

    context.startActivity(shareIntent)

}
