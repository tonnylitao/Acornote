package tonnysunm.com.acornote.widget

import android.appwidget.AppWidgetManager
import android.content.*

private val TAG = "ScreenActionReceiver"

class ScreenActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val ctx = context ?: return
        val action = intent?.action ?: return

        if (action == Intent.ACTION_USER_PRESENT) {
            val widgetIntent = Intent(ctx, ScreenWidget::class.java).apply {
                this.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE

                val ids = AppWidgetManager.getInstance(ctx)
                    .getAppWidgetIds(ComponentName(ctx, ScreenWidget::class.java))

                this.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            }

            ctx.sendBroadcast(widgetIntent)
        }
    }

    fun getFilter() = IntentFilter().apply {
        addAction(Intent.ACTION_USER_PRESENT)
    }
}