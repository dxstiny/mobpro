package ch.hslu.diashorta.sw07

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.util.Log
import android.widget.RemoteViews

/**
 * Implementation of App Widget functionality.
 */
class MyAppWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val text = getTextFromPres(context)
        Log.d("MyAppWidget", "onUpdate: $text")

        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.my_app_widget)
            views.setTextViewText(
                R.id.appwidget_text,
                "$text\nCount: ${updateCount++}"
            )
            Log.d("MyAppWidget", "updated: $text")
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        Log.d("MyAppWidget", "updated all widgets")
    }

    private fun getTextFromPres(context: Context): String? {
        val prefs = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(WIDGET_TEXT_PREFS_KEY, "default")
    }

    companion object {
        private var updateCount = 0
        const val WIDGET_TEXT_PREFS_KEY = "widget_text"
        const val MY_PREFS_NAME = "my_prefs"
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
