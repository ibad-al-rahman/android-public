package com.ibadalrahman.widgets.prayertimes

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.ibadalrahman.widgets.prayertimes.view.PrayerTimesWidgetRootScreen

class PrayerTimesWidgetReceiver: GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = PrayerTimesWidgetRootScreen()
}
