package com.ibadalrahman.widgets.prayertimes

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.Text
import com.ibadalrahman.prayertimes.repository.data.domain.DayPrayerTimes
import com.ibadalrahman.resources.R
import com.ibadalrahman.widgets.prayertimes.theme.WidgetGlanceColorScheme
import com.ibadalrahman.widgets.prayertimes.theme.WidgetGlanceTheme
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
    fun PrayerTimesWidget(
        dayPrayerTimes: DayPrayerTimes?
    ) {
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
                Row(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    LazyColumn(modifier = GlanceModifier.defaultWeight()) {
                        item {
                            PrayerTimeRow(
                                label = localizedString(R.string.fajr),
                                time = dayPrayerTimes.prayerTimes.fajr
                            )
                        }
                        item {
                            PrayerTimeRow(
                                label = localizedString(R.string.sunrise),
                                time = dayPrayerTimes.prayerTimes.sunrise
                            )
                        }
                        item {
                            PrayerTimeRow(
                                label = localizedString(R.string.dhuhr),
                                time = dayPrayerTimes.prayerTimes.dhuhr
                            )
                        }
                        item {
                            PrayerTimeRow(
                                label = localizedString(R.string.asr),
                                time = dayPrayerTimes.prayerTimes.asr
                            )
                        }
                        item {
                            PrayerTimeRow(
                                label = localizedString(R.string.maghrib),
                                time = dayPrayerTimes.prayerTimes.maghrib
                            )
                        }
                        item {
                            PrayerTimeRow(
                                label = localizedString(R.string.ishaa),
                                time = dayPrayerTimes.prayerTimes.ishaa
                            )
                        }
                    }

                    Column(
                        modifier = GlanceModifier.defaultWeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.Vertical.Bottom
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

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = GlanceModifier.fillMaxHeight()
                            ) {
                                Text(
                                    text = formatGregorianDate(dayPrayerTimes.gregorian),
                                    style = WidgetGlanceTypography.bodyMedium.copy(
                                        color = WidgetGlanceColorScheme.onSurface
                                    )
                                )
                                Text(
                                    text = dayPrayerTimes.hijri,
                                    style = WidgetGlanceTypography.bodySmall.copy(
                                        color = WidgetGlanceColorScheme.onSurfaceVariant
                                    )
                                )
                                Spacer(modifier = GlanceModifier.defaultWeight())

                                val currentPrayer = getCurrentPrayer(dayPrayerTimes)
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = GlanceModifier.padding(bottom = 12.dp)
                                ) {
                                    Text(
                                        text = localizedString(R.string.current_prayer_time),
                                        style = WidgetGlanceTypography.labelSmall.copy(
                                            color = WidgetGlanceColorScheme.onSurfaceVariant
                                        )
                                    )
                                    Text(
                                        text = getPrayerName(currentPrayer),
                                        style = WidgetGlanceTypography.titleMedium.copy(
                                            color = WidgetGlanceColorScheme.primary
                                        )
                                    )
                                }
                            }
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
    private fun PrayerTimeRow(label: String, time: Date) {
        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(vertical = 3.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = label,
                style = WidgetGlanceTypography.bodySmall.copy(
                    color = WidgetGlanceColorScheme.onSurface
                )
            )
            Spacer(modifier = GlanceModifier.defaultWeight())
            Text(
                text = formatTime(time),
                style = WidgetGlanceTypography.bodySmall.copy(
                    color = WidgetGlanceColorScheme.onSurface
                )
            )
        }
    }

    @Composable
    private fun localizedString(resId: Int): String {
        return LocalContext.current.getString(resId)
    }

    @Composable
    private fun getPrayerName(prayer: Prayer): String {
        return when (prayer) {
            Prayer.FAJR -> localizedString(R.string.fajr)
            Prayer.SUNRISE -> localizedString(R.string.sunrise)
            Prayer.DHUHR -> localizedString(R.string.dhuhr)
            Prayer.ASR -> localizedString(R.string.asr)
            Prayer.MAGHRIB -> localizedString(R.string.maghrib)
            Prayer.ISHAA -> localizedString(R.string.ishaa)
        }
    }

    private fun formatTime(date: Date): String {
        val formatter =  DateFormat.getTimeInstance(DateFormat.SHORT)
        return formatter.format(date)
    }

    private fun formatGregorianDate(date: Date): String {
        val formatter = DateFormat.getDateInstance(DateFormat.MEDIUM)
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
