package org.ibadalrahman.publicsector

import Prayer
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.LayoutDirection
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RemoteViews
import android.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.core.os.ConfigurationCompat
import androidx.core.os.LocaleListCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.ibadalrahman.publicsector.databinding.PrayerAppWidgetBinding
import org.ibadalrahman.publicsector.main.model.PrayerData
import org.ibadalrahman.publicsector.main.model.PrayerRepositoryImpl
import org.ibadalrahman.publicsector.main.view.MainActivity
import org.ibadalrahman.publicsector.main.view.computeTimeDiffFromNow
import org.ibadalrahman.publicsector.main.view.displayTime
import org.ibadalrahman.publicsector.main.view.getLocale
import org.ibadalrahman.publicsector.main.view.getLocalizedPrayerName
import java.text.SimpleDateFormat
import java.time.chrono.HijrahChronology
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.collections.get
import kotlin.math.abs
import kotlin.math.ceil

/**
 * Implementation of App Widget functionality.
 */
class PrayerAppWidget : AppWidgetProvider() {

//    key = widget id ; value = layout (normal or mini)
//    private var widget_layouts: HashMap<Int, Int> = HashMap<Int, Int>()
    private var prayerData: PrayerData = PrayerData()
    private var currentDate = ""
    private var repository: PrayerRepositoryImpl = PrayerRepositoryImpl()
    private var dateFormatter: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy", java.util.Locale.ROOT)


    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        for (appWidgetId in appWidgetIds) {
            // update clock
            updateAppWidget(context, appWidgetManager, appWidgetId)

            // set intent for when user clicks on widget --> open app
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                context,
                appWidgetId, // <- different code for each
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val views = RemoteViews(context.packageName, R.layout.prayer_app_widget)
            views.setOnClickPendingIntent(R.id.root_layout, pendingIntent)
        }

//        update prayer times
        var currentDate = this.dateFormatter.format(Date())
        this.currentDate = currentDate
        fetchPrayerData { prayerData ->
            this.prayerData = prayerData
            // There may be multiple widgets active, so update all of them
            for (appWidgetId in appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId)
            }

        }



    }

    private fun fetchPrayerData(callback: (PrayerData) -> Unit) {
        runBlocking {
            async {              // Dispatchers.IO (main-safety block)
                /* perform network IO here */          // Dispatchers.IO (main-safety block)
                var prayerTimes: PrayerData = repository.getPrayersForDay(currentDate)
                callback(prayerTimes)
            }
        }

    }

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        if(newOptions != null) {
            val minWidth = newOptions?.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
            val minHeight = newOptions?.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);

            val widget_width = ceil(minWidth!! / 120.0)
            val widget_height = ceil(minHeight!! / 150.0)
            println("WIDGET WIDTH = $widget_width")
            println("WIDGET HEIGHT = $widget_height")

            if(widget_height >= 2 && widget_width >= 4) {
//                this.widget_layouts[appWidgetId] = R.layout.prayer_app_widget
            }
            else {
//                this.widget_layouts[appWidgetId] = R.layout.prayer_app_widget_mini

            }
            updateAppWidget(context!!, appWidgetManager!!, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created

    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }


//    called at every update interval
    internal fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
    ) {
//        val views = RemoteViews(context.packageName, this.widget_layouts[appWidgetId] ?: R.layout.prayer_app_widget)
        val views = RemoteViews(context.packageName, getLayoutForCurrentLocale(context))
//      get nearest prayer
        var nearestPrayerName : String = ""
        var nearestPrayerId: String = ""
        var minDiffTime = Long.MAX_VALUE
        for (p in this.prayerData.prayerTimes) {
            val timeDiff = computeTimeDiffFromNow(SimpleDateFormat("hh:mm a", java.util.Locale.ROOT).parse(p.time)!!)
            println("timeDiff = $timeDiff")
            println("minTimeDiff = $minDiffTime")
            if(abs(timeDiff) < abs(minDiffTime)) {
                minDiffTime = timeDiff
                nearestPrayerName = getLocalizedPrayerName(context, p.name)
                nearestPrayerId = p.name.lowercase()
            }
        }

        println("Nearest Prayer Name = $nearestPrayerName")
        views.setTextViewText(R.id.chronometer_label, nearestPrayerName)

//    chronometer
    if(minDiffTime != Long.MAX_VALUE) {
        val cal = Calendar.getInstance()
        cal.timeZone = TimeZone.getTimeZone("UTC")
        cal.timeInMillis = abs(minDiffTime)
        val timeDiffMillis = cal.get(Calendar.HOUR_OF_DAY) * 3600000 + cal.get(Calendar.MINUTE) * 60000
        var base = SystemClock.elapsedRealtime() - timeDiffMillis

        if(minDiffTime < 0) {
            base = SystemClock.elapsedRealtime() + timeDiffMillis
//            views.setChronometerCountDown(R.id.chronometer, true)
        }
        views.setChronometer(R.id.chronometer, base, "%tH:%tM:%tS", true)
    }

    views.setInt(R.id.fajr_row, "setBackgroundColor", Color.WHITE)
    views.setTextColor(R.id.fajr_label, Color.BLACK)
    views.setTextColor(R.id.fajr_value, Color.BLACK)

    views.setInt(R.id.sunrise_row, "setBackgroundColor", Color.WHITE)
    views.setTextColor(R.id.sunrise_label, Color.BLACK)
    views.setTextColor(R.id.sunrise_value, Color.BLACK)

    views.setInt(R.id.dhuhr_row, "setBackgroundColor", Color.WHITE)
    views.setTextColor(R.id.dhuhr_label, Color.BLACK)
    views.setTextColor(R.id.dhuhr_value, Color.BLACK)

    views.setInt(R.id.asr_row, "setBackgroundColor", Color.WHITE)
    views.setTextColor(R.id.asr_label, Color.BLACK)
    views.setTextColor(R.id.asr_value, Color.BLACK)

    views.setInt(R.id.maghrib_row, "setBackgroundColor", Color.WHITE)
    views.setTextColor(R.id.maghrib_label, Color.BLACK)
    views.setTextColor(R.id.maghrib_value, Color.BLACK)

    views.setInt(R.id.ishaa_row, "setBackgroundColor", Color.WHITE)
    views.setTextColor(R.id.ishaa_label, Color.BLACK)
    views.setTextColor(R.id.ishaa_value, Color.BLACK)



//    highlight current nearest prayer row
        when(nearestPrayerId) {
            "fajr" -> {
                views.setInt(R.id.fajr_row, "setBackgroundResource", R.drawable.prayer_row_background)
                views.setTextColor(R.id.fajr_label, Color.WHITE)
                views.setTextColor(R.id.fajr_value, Color.WHITE)
            }
            "sunrise" -> {
                views.setInt(R.id.sunrise_row, "setBackgroundResource", R.drawable.prayer_row_background)
                views.setTextColor(R.id.sunrise_label, Color.WHITE)
                views.setTextColor(R.id.sunrise_value, Color.WHITE)
            }
            "dhuhr" -> {
                views.setInt(R.id.dhuhr_row, "setBackgroundResource", R.drawable.prayer_row_background)
                views.setTextColor(R.id.dhuhr_label, Color.WHITE)
                views.setTextColor(R.id.dhuhr_value, Color.WHITE)
            }
            "asr" -> {
                views.setInt(R.id.asr_row, "setBackgroundResource", R.drawable.prayer_row_background)
                views.setTextColor(R.id.asr_label, Color.WHITE)
                views.setTextColor(R.id.asr_value, Color.WHITE)
            }
            "maghrib" -> {
                views.setInt(R.id.maghrib_row, "setBackgroundResource", R.drawable.prayer_row_background)
                views.setTextColor(R.id.maghrib_label, Color.WHITE)
                views.setTextColor(R.id.maghrib_value, Color.WHITE)
            }
            "ishaa" -> {
                views.setInt(R.id.ishaa_row, "setBackgroundResource", R.drawable.prayer_row_background)
                views.setTextColor(R.id.ishaa_label, Color.WHITE)
                views.setTextColor(R.id.ishaa_value, Color.WHITE)
            }
        }

//        update prayer times
    val prayers = this.prayerData.prayerTimes

//    update prayer labels
    views.setTextViewText(R.id.fajr_label, context.resources.getString(R.string.fajr))
    views.setTextViewText(R.id.sunrise_label, context.resources.getString(R.string.sunrise))
    views.setTextViewText(R.id.dhuhr_label, context.resources.getString(R.string.dhuhr))
    views.setTextViewText(R.id.asr_label, context.resources.getString(R.string.asr))
    views.setTextViewText(R.id.maghrib_label, context.resources.getString(R.string.maghrib))
    views.setTextViewText(R.id.ishaa_label, context.resources.getString(R.string.ishaa))

    if(prayers.size >= 6) {
        views.setTextViewText(R.id.fajr_value, prayers[0].time
            .replace("am",  context.resources.getString(R.string.am))
            .replace("pm", context.resources.getString(R.string.pm)))

        views.setTextViewText(R.id.sunrise_value, prayers[1].time
            .replace("am",  context.resources.getString(R.string.am))
            .replace("pm", context.resources.getString(R.string.pm)))

        views.setTextViewText(R.id.dhuhr_value, prayers[2].time
            .replace("am",  context.resources.getString(R.string.am))
            .replace("pm", context.resources.getString(R.string.pm)))

        views.setTextViewText(R.id.asr_value, prayers[3].time
            .replace("am",  context.resources.getString(R.string.am))
            .replace("pm", context.resources.getString(R.string.pm)))

        views.setTextViewText(R.id.maghrib_value, prayers[4].time
            .replace("am",  context.resources.getString(R.string.am))
            .replace("pm", context.resources.getString(R.string.pm)))

        views.setTextViewText(R.id.ishaa_value, prayers[5].time
            .replace("am",  context.resources.getString(R.string.am))
            .replace("pm", context.resources.getString(R.string.pm)))
    }

//    format hijjri & gregorian dates
    if(prayerData.hijri != "") {
        val inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val gregorianDate = inputFormatter.parse(prayerData.gregorian)

        val hijriDate = HijrahChronology.INSTANCE.date(inputFormatter.withChronology(HijrahChronology.INSTANCE).parse(prayerData.hijri))


        val outputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy").withLocale(Locale.forLanguageTag("ar-LB"))
        val gregorianDateFormatted = outputFormatter.format(gregorianDate)
        val hijriDateFormatted = outputFormatter.format(hijriDate)

        views.setTextViewText(R.id.date_hijri, hijriDateFormatted)
        views.setTextViewText(R.id.date_gregorian, gregorianDateFormatted)
    }

        // Instruct the widget manager to update the widget
        if(prayerData.prayerTimes.size > 0) {
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }





    fun getLocalizedPrayerName(context: Context, name: String): String {
        when(name) {
            "Fajr" -> return context.resources.getString(R.string.fajr)
            "Sunrise" -> return context.resources.getString(R.string.sunrise)
            "Dhuhr" -> return context.resources.getString(R.string.dhuhr)
            "Asr" -> return context.resources.getString(R.string.asr)
            "Maghrib" -> return context.resources.getString(R.string.maghrib)
            "Ishaa" -> return context.resources.getString(R.string.ishaa)
            else -> return name
        }
    }

    fun getLayoutForCurrentLocale(context: Context): Int {
        val locale = getCurrentLocale(context)
        if(locale == Locale.forLanguageTag("ar")) {
            return R.layout.prayer_app_widget_ar
        }
        else {
            return R.layout.prayer_app_widget
        }
    }

    fun getCurrentLocale(context: Context): Locale {
        return context.resources.configuration.locales.get(0)
    }

}






