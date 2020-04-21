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
}
