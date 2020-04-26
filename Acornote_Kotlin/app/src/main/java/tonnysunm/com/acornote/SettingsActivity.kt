package tonnysunm.com.acornote

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import kotlinx.android.synthetic.main.activity_note.*
import tonnysunm.com.acornote.service.BubbleService

class SettingsActivity : AppCompatActivity(R.layout.settings_activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)
        //set drawer icon
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        private val INTENT_REQUEST_CODE = 10

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            preferenceManager.findPreference<SwitchPreferenceCompat>("settings_overlay")
                ?.setOnPreferenceChangeListener { _, newValue ->
                    val bool: Boolean = newValue as Boolean

                    enableOverOnApps(bool)
                }
        }

        private fun enableOverOnApps(enabled: Boolean): Boolean {
            val activity = requireActivity()

            if (enabled) {
                if (!Settings.canDrawOverlays(activity)) {
                    //Android system bug, cannot go back to this app when click back button in toolbar
                    startActivityForResult(
                        Intent(
                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:${activity.packageName}")
                        ), INTENT_REQUEST_CODE
                    )
                    return false
                } else {
                    activity.startService(Intent(activity, BubbleService::class.java))
                }
            } else {
                requireActivity().stopService(Intent(activity, BubbleService::class.java))
            }

            return true
        }

        override fun onResume() {
            val isChecked = Settings.canDrawOverlays(requireContext())

            preferenceManager.findPreference<SwitchPreferenceCompat>("overlay")?.isChecked =
                isChecked

            super.onResume()
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            val ctx = context ?: return

            val isChecked = Settings.canDrawOverlays(ctx)
            if (requestCode == INTENT_REQUEST_CODE) {
                if (isChecked) {
                    ctx.startService(Intent(ctx, BubbleService::class.java))
                } else {
                    ctx.stopService(Intent(ctx, BubbleService::class.java))
                }
            }

            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}


