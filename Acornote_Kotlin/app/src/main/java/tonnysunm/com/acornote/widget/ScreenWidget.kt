package tonnysunm.com.acornote.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tonnysunm.com.acornote.R
import tonnysunm.com.acornote.model.Repository
import tonnysunm.com.acornote.ui.note.NoteActivity

class ScreenWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {

        //
        val repository = Repository(context)
        GlobalScope.launch(Dispatchers.IO) {
            val note = repository.noteDao.getRandom()

            note?.let {
                val views = RemoteViews(context.packageName, R.layout.screen_widget)

                val intent = Intent(context, NoteActivity::class.java).apply {
                    putExtra("id", it.id)
                }
                val pendingIntent =
                    PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                views.setOnClickPendingIntent(R.id.title_textView, pendingIntent)


                GlobalScope.launch(Dispatchers.Main) {
                    views.setTextViewText(R.id.title_textView, it.title)
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            }
        }

    }
}