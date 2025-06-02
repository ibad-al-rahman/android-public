package com.ibadalrahman.widgets.prayertimes

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class HelloWorldWidgetProvider: AppWidgetProvider() {
    companion object {
        private const val TAG = "HelloWorldWidget"
    }
    
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WidgetEntryPoint {
        fun viewModel(): HelloWorldWidgetViewModel
    }

    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d(TAG, "onUpdate called with ${appWidgetIds.size} widgets")
        
        // Schedule periodic updates
        HelloWorldWidgetUpdateWorker.scheduleUpdates(context)
        
        appWidgetIds.forEach { appWidgetId ->
            coroutineScope.launch {
                try {
                    Log.d(TAG, "Updating widget $appWidgetId")
                    updateWidget(context, appWidgetManager, appWidgetId)
                } catch (e: Exception) {
                    Log.e(TAG, "Error updating widget $appWidgetId", e)
                    showErrorWidget(context, appWidgetManager, appWidgetId, e.message ?: "Unknown error")
                }
            }
        }
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        Log.d(TAG, "onEnabled - First widget added")
        HelloWorldWidgetUpdateWorker.scheduleUpdates(context)
    }
    
    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        Log.d(TAG, "onDisabled - Last widget removed")
        job.cancel()
        HelloWorldWidgetUpdateWorker.cancelUpdates(context)
    }

    private suspend fun updateWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.hello_world_widget_layout)

        try {
            Log.d(TAG, "Getting ViewModel from EntryPoint")
            
            val entryPoint = EntryPoints.get(
                context.applicationContext,
                WidgetEntryPoint::class.java
            )
            val viewModel = entryPoint.viewModel()
            
            Log.d(TAG, "Fetching prayer times from ViewModel")
            
            // Get prayer times from ViewModel
            viewModel.getPrayerTimes().fold(
                onSuccess = { prayerData ->
                    Log.d(TAG, "Successfully fetched prayer times: ${prayerData.prayerTimes.size} prayers")
                    
                    // Update date
                    views.setTextViewText(R.id.date_text, prayerData.date)
                    
                    // Update next prayer info
                    prayerData.nextPrayer?.let { nextPrayer ->
                        views.setTextViewText(R.id.next_prayer_label, context.getString(com.ibadalrahman.resources.R.string.next_prayer))
                        views.setTextViewText(R.id.next_prayer_name, getLocalizedPrayerName(context, nextPrayer.prayerName))
                        views.setChronometerCountDown(R.id.next_prayer_time, true)
                        views.setChronometer(R.id.next_prayer_time, nextPrayer.chroneterBaseTime, null, true)
                    } ?: run {
                        views.setTextViewText(R.id.next_prayer_label, "")
                        views.setTextViewText(R.id.next_prayer_name, "")
                        views.setChronometer(R.id.next_prayer_time, 0, null, false)
                    }
                    
                    // Update prayer names and times
                    val prayers = HelloWorldWidgetViewModel.Prayer.values()
                    prayers.forEachIndexed { index, prayer ->
                        val time = prayerData.prayerTimes[prayer] ?: ""
                        val localizedTime = viewModel.getLocalizedTime(time)
                        
                        Log.d(TAG, "Prayer ${prayer.name}: $time (localized: $localizedTime)")
                        
                        when (index) {
                            0 -> {
                                views.setTextViewText(R.id.text1, context.getString(prayer.stringResId))
                                views.setTextViewText(R.id.time1, localizedTime)
                            }
                            1 -> {
                                views.setTextViewText(R.id.text2, context.getString(prayer.stringResId))
                                views.setTextViewText(R.id.time2, localizedTime)
                            }
                            2 -> {
                                views.setTextViewText(R.id.text3, context.getString(prayer.stringResId))
                                views.setTextViewText(R.id.time3, localizedTime)
                            }
                            3 -> {
                                views.setTextViewText(R.id.text4, context.getString(prayer.stringResId))
                                views.setTextViewText(R.id.time4, localizedTime)
                            }
                            4 -> {
                                views.setTextViewText(R.id.text5, context.getString(prayer.stringResId))
                                views.setTextViewText(R.id.time5, localizedTime)
                            }
                            5 -> {
                                views.setTextViewText(R.id.text6, context.getString(prayer.stringResId))
                                views.setTextViewText(R.id.time6, localizedTime)
                            }
                        }
                    }
                },
                onFailure = { error ->
                    Log.e(TAG, "Failed to fetch prayer times", error)
                    // Show error state
                    views.setTextViewText(R.id.date_text, "")
                    views.setTextViewText(R.id.next_prayer_label, "")
                    views.setTextViewText(R.id.next_prayer_name, "")
                    views.setChronometer(R.id.next_prayer_time, 0, null, false)
                    views.setTextViewText(R.id.text1, "Error loading")
                    views.setTextViewText(R.id.text2, "prayer times")
                    views.setTextViewText(R.id.text3, error.message ?: "")
                    views.setTextViewText(R.id.text4, "")
                    views.setTextViewText(R.id.text5, "")
                    views.setTextViewText(R.id.text6, "")
                    views.setTextViewText(R.id.time1, "")
                    views.setTextViewText(R.id.time2, "")
                    views.setTextViewText(R.id.time3, "")
                    views.setTextViewText(R.id.time4, "")
                    views.setTextViewText(R.id.time5, "")
                    views.setTextViewText(R.id.time6, "")
                }
            )
        } catch (e: Exception) {
            Log.e(TAG, "Exception in updateWidget", e)
            throw e
        }

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
    
    private fun getLocalizedPrayerName(context: Context, prayerName: String): String {
        return when (prayerName) {
            HelloWorldWidgetViewModel.Prayer.FAJR.name -> context.getString(com.ibadalrahman.resources.R.string.fajr)
            HelloWorldWidgetViewModel.Prayer.SUNRISE.name -> context.getString(com.ibadalrahman.resources.R.string.sunrise)
            HelloWorldWidgetViewModel.Prayer.DHUHR.name -> context.getString(com.ibadalrahman.resources.R.string.dhuhr)
            HelloWorldWidgetViewModel.Prayer.ASR.name -> context.getString(com.ibadalrahman.resources.R.string.asr)
            HelloWorldWidgetViewModel.Prayer.MAGHRIB.name -> context.getString(com.ibadalrahman.resources.R.string.maghrib)
            HelloWorldWidgetViewModel.Prayer.ISHAA.name -> context.getString(com.ibadalrahman.resources.R.string.ishaa)
            else -> prayerName
        }
    }
    
    private fun showErrorWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        errorMessage: String
    ) {
        val views = RemoteViews(context.packageName, R.layout.hello_world_widget_layout)
        views.setTextViewText(R.id.date_text, "")
        views.setTextViewText(R.id.next_prayer_label, "")
        views.setTextViewText(R.id.next_prayer_name, "")
        views.setChronometer(R.id.next_prayer_time, 0, null, false)
        views.setTextViewText(R.id.text1, "Error:")
        views.setTextViewText(R.id.text2, errorMessage)
        views.setTextViewText(R.id.text3, "")
        views.setTextViewText(R.id.text4, "")
        views.setTextViewText(R.id.text5, "")
        views.setTextViewText(R.id.text6, "")
        views.setTextViewText(R.id.time1, "")
        views.setTextViewText(R.id.time2, "")
        views.setTextViewText(R.id.time3, "")
        views.setTextViewText(R.id.time4, "")
        views.setTextViewText(R.id.time5, "")
        views.setTextViewText(R.id.time6, "")
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
