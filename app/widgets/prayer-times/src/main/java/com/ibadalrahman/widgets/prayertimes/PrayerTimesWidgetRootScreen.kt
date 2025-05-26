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
import androidx.glance.action.clickable
import androidx.glance.background
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.layout.Box
import androidx.glance.layout.ContentScale
import androidx.glance.layout.size
import androidx.glance.unit.ColorProvider
import androidx.glance.appwidget.cornerRadius
import androidx.compose.ui.graphics.toArgb
import android.content.ComponentName
import android.content.Intent
import androidx.compose.ui.graphics.Color
import androidx.glance.layout.fillMaxSize
import com.ibadalrahman.prayertimes.repository.data.domain.DayPrayerTimes
import com.ibadalrahman.resources.R
import com.ibadalrahman.widgets.prayertimes.theme.WidgetGlanceTheme
import com.ibadalrahman.widgets.prayertimes.theme.WidgetGlanceColorScheme
import com.ibadalrahman.widgets.prayertimes.theme.WidgetGlanceTypography
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.DateFormat
import java.util.Date

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
            WidgetGlanceTheme {
                PrayerTimesWidget(dayPrayerTimes = dayPrayerTimes)
            }
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
                .background(WidgetGlanceColorScheme.background)
                .clickable(
                    onClick = actionStartActivity(intent)
                )
        ) {
            if (dayPrayerTimes != null) {
                Row(modifier = GlanceModifier.fillMaxWidth()) {
                    Column(
                        modifier = GlanceModifier.defaultWeight()
                    ) {
                        val currentPrayer = getCurrentPrayer(dayPrayerTimes)

                        PrayerTimeRow(
                            label = localizedString(R.string.fajr),
                            time = dayPrayerTimes.prayerTimes.fajr,
                            isActive = currentPrayer == Prayer.FAJR
                        )
                        PrayerTimeRow(
                            label = localizedString(R.string.sunrise),
                            time = dayPrayerTimes.prayerTimes.sunrise,
                            isActive = currentPrayer == Prayer.SUNRISE
                        )
                        PrayerTimeRow(
                            label = localizedString(R.string.dhuhr),
                            time = dayPrayerTimes.prayerTimes.dhuhr,
                            isActive = currentPrayer == Prayer.DHUHR
                        )
                        PrayerTimeRow(
                            label = localizedString(R.string.asr),
                            time = dayPrayerTimes.prayerTimes.asr,
                            isActive = currentPrayer == Prayer.ASR
                        )
                        PrayerTimeRow(
                            label = localizedString(R.string.maghrib),
                            time = dayPrayerTimes.prayerTimes.maghrib,
                            isActive = currentPrayer == Prayer.MAGHRIB
                        )
                        PrayerTimeRow(
                            label = localizedString(R.string.ishaa),
                            time = dayPrayerTimes.prayerTimes.ishaa,
                            isActive = currentPrayer == Prayer.ISHAA
                        )
                    }
                    Column(
                        modifier = GlanceModifier.defaultWeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = GlanceModifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                provider = ImageProvider(R.drawable.ic_launcher_watermark),
                                contentDescription = "App Icon Watermark",
                                modifier = GlanceModifier.fillMaxSize()
                            )

                            Text(
                                text = "Hello world",
                                style = WidgetGlanceTypography.bodyMedium.copy(
                                    color = WidgetGlanceColorScheme.onSurface
                                )
                            )
                        }
                    }
                }
            } else {
                Column(
                    modifier = GlanceModifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = localizedString(R.string.loading_prayer_times),
                        style = WidgetGlanceTypography.bodyMedium.copy(
                            color = WidgetGlanceColorScheme.onSurface
                        )
                    )
                }
            }
        }
    }

    @Composable
    private fun PrayerTimeRow(label: String, time: Date, isActive: Boolean = false) {
        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(vertical = 2.dp)
                .then(
                    if (isActive) {
                        GlanceModifier
                            .cornerRadius(12.dp)
                            .background(WidgetGlanceColorScheme.primary)
                            .padding(horizontal = 12.dp, vertical = 1.dp)
                    } else {
                        GlanceModifier.padding(horizontal = 12.dp, vertical = 1.dp)
                    }
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = label,
                style = WidgetGlanceTypography.bodySmall.copy(
                    color = if (isActive) WidgetGlanceColorScheme.onPrimary else WidgetGlanceColorScheme.onSurface
                )
            )
            Spacer(modifier = GlanceModifier.defaultWeight())
            Text(
                text = formatTime(time),
                style = WidgetGlanceTypography.bodySmall.copy(
                    color = if (isActive) WidgetGlanceColorScheme.onPrimary else WidgetGlanceColorScheme.onSurface
                )
            )
        }
    }

    @Composable
    private fun localizedString(resId: Int): String {
        return LocalContext.current.getString(resId)
    }

    private fun formatTime(date: Date): String {
        val formatter =  DateFormat.getTimeInstance(DateFormat.SHORT)
        return formatter.format(date)
    }

    private fun getCurrentPrayer(dayPrayerTimes: DayPrayerTimes): Prayer {
        val now = Date()
        val prayerTimes = listOf(
            Prayer.FAJR to dayPrayerTimes.prayerTimes.fajr,
            Prayer.SUNRISE to dayPrayerTimes.prayerTimes.sunrise,
            Prayer.DHUHR to dayPrayerTimes.prayerTimes.dhuhr,
            Prayer.ASR to dayPrayerTimes.prayerTimes.asr,
            Prayer.MAGHRIB to dayPrayerTimes.prayerTimes.maghrib,
            Prayer.ISHAA to dayPrayerTimes.prayerTimes.ishaa
        )

        for ((prayer, time) in prayerTimes.reversed()) {
            if (time.before(now) || time == now) {
                return prayer
            }
        }

        return Prayer.ISHAA
    }

    enum class Prayer {
        FAJR,
        SUNRISE,
        DHUHR,
        ASR,
        MAGHRIB,
        ISHAA
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    fun interface PrayerTimesWidgetEntryPoint {
        fun getViewModel(): PrayerTimesWidgetViewModel
    }
}
