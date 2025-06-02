package com.ibadalrahman.widgets.prayertimes

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HelloWorldWidgetProvider: AppWidgetProvider() {
    @Inject
    lateinit var viewModel: HelloWorldWidgetViewModel

    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { appWidgetId ->
            coroutineScope.launch {
                updateWidget(context, appWidgetManager, appWidgetId)
            }
        }
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        job.cancel()
    }

    private fun updateWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.hello_world_widget_layout)

        // Get data from ViewModel
        val leftTexts = viewModel.getLeftTexts()
        val rightText = viewModel.getRightText()

        // Update left side texts
        views.setTextViewText(R.id.text1, leftTexts.getOrNull(0) ?: "")
        views.setTextViewText(R.id.text2, leftTexts.getOrNull(1) ?: "")
        views.setTextViewText(R.id.text3, leftTexts.getOrNull(2) ?: "")
        views.setTextViewText(R.id.text4, leftTexts.getOrNull(3) ?: "")
        views.setTextViewText(R.id.text5, leftTexts.getOrNull(4) ?: "")

        // Update right side text
        views.setTextViewText(R.id.hello_world_text, rightText)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
