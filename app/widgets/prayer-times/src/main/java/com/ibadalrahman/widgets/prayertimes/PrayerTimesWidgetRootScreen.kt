package com.ibadalrahman.widgets.prayertimes

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.glance.GlanceTheme
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionStartActivity
import android.content.ComponentName
import android.content.Intent
import com.ibadalrahman.prayertimes.repository.data.domain.DayPrayerTimes
import com.ibadalrahman.prayertimes.repository.data.domain.PrayerTimes
import com.ibadalrahman.resources.R
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PrayerTimesWidgetRootScreen: GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val appContext = context.applicationContext
        val vm = EntryPoints
            .get(appContext, PrayerTimesWidgetEntryPoint::class.java)
            .getViewModel()

        val dayPrayerTimes = withContext(Dispatchers.IO) {
            vm.getPrayerTimes().getOrNull()
        }

        provideContent {
            PrayerTimesWidget(dayPrayerTimes = dayPrayerTimes)
        }
    }

    @Composable
    fun PrayerTimesWidget(dayPrayerTimes: DayPrayerTimes?) {
        val context = LocalContext.current
        val intent = Intent().apply {
            component = ComponentName(
                context.packageName,
                "org.ibadalrahman.publicsector.main.view.MainActivity"
            )
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        
        Scaffold(
            modifier = GlanceModifier
                .padding(16.dp)
                .clickable(
                    onClick = actionStartActivity(intent)
                )
        ) {
            if (dayPrayerTimes != null) {
                Column(
                    modifier = GlanceModifier.fillMaxWidth()
                ) {
                    Text(
                        text = formatHijriDate(dayPrayerTimes.hijri),
                        style = TextStyle(
                            color = GlanceTheme.colors.onSurface
                        )
                    )
                    
                    Spacer(modifier = GlanceModifier.padding(8.dp))
                    
                    PrayerTimeRow(
                        label = localizedString(R.string.fajr),
                        time = dayPrayerTimes.prayerTimes.fajr
                    )
                    PrayerTimeRow(
                        label = localizedString(R.string.sunrise),
                        time = dayPrayerTimes.prayerTimes.sunrise
                    )
                    PrayerTimeRow(
                        label = localizedString(R.string.dhuhr),
                        time = dayPrayerTimes.prayerTimes.dhuhr
                    )
                    PrayerTimeRow(
                        label = localizedString(R.string.asr),
                        time = dayPrayerTimes.prayerTimes.asr
                    )
                    PrayerTimeRow(
                        label = localizedString(R.string.maghrib),
                        time = dayPrayerTimes.prayerTimes.maghrib
                    )
                    PrayerTimeRow(
                        label = localizedString(R.string.ishaa),
                        time = dayPrayerTimes.prayerTimes.ishaa
                    )
                }
            } else {
                Column(
                    modifier = GlanceModifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = localizedString(R.string.loading_prayer_times),
                        style = TextStyle(
                            color = GlanceTheme.colors.onSurface
                        )
                    )
                }
            }
        }
    }

    @Composable
    private fun PrayerTimeRow(label: String, time: Date) {
        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = label,
                style = TextStyle(
                    color = GlanceTheme.colors.onSurface
                )
            )
            Spacer(modifier = GlanceModifier.defaultWeight())
            Text(
                text = formatTime(time),
                style = TextStyle(
                    color = GlanceTheme.colors.onSurface
                )
            )
        }
    }

    @Composable
    private fun localizedString(resId: Int): String {
        return LocalContext.current.getString(resId)
    }

    private fun formatTime(date: Date): String {
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        return formatter.format(date)
    }

    private fun formatHijriDate(hijri: String): String {
        return hijri
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    fun interface PrayerTimesWidgetEntryPoint {
        fun getViewModel(): PrayerTimesWidgetViewModel
    }
}
