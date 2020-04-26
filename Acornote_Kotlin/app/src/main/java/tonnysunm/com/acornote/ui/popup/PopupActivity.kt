package tonnysunm.com.acornote.ui.popup

import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import tonnysunm.com.acornote.R


class PopupActivity : AppCompatActivity(R.layout.activity_popup) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL)
    }

    //https://developer.android.com/about/versions/10/privacy/changes#clipboard-data
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        if (hasFocus) {
            val fragment =
                supportFragmentManager.findFragmentById(R.id.fragment_edit_note_popup) as? PopupFragment
            fragment?.onWindowFocus()
        }
    }
}
