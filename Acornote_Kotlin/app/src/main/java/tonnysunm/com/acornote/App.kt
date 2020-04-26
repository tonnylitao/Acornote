package tonnysunm.com.acornote

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import androidx.preference.PreferenceManager
import tonnysunm.com.acornote.ui.popup.InvisibleActivity
import tonnysunm.com.acornote.widget.ScreenWidget


/**
 *
 */

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        val receiver = ScreenWidget()
        registerReceiver(receiver, receiver.getFilter())

        updateInvisibleComponent(this)
    }

    companion object {
        private fun updateInvisibleComponent(ctx: Context) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(ctx)
            val enabled = prefs.getBoolean("settings_invisible", false)

            updateInvisibleComponent(ctx, enabled)
        }

        fun updateInvisibleComponent(ctx: Context, enabled: Boolean) {
            val state =
                if (enabled) PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                else PackageManager.COMPONENT_ENABLED_STATE_DISABLED

            ctx.packageManager.setComponentEnabledSetting(
                ComponentName(ctx, InvisibleActivity::class.java),
                state,
                PackageManager.DONT_KILL_APP
            )
        }
    }
}