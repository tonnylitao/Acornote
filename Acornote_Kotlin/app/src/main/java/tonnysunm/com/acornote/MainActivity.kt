package tonnysunm.com.acornote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import tonnysunm.com.acornote.ui.main.MainFragment

/*TODO

Navigation
Recycle view
Room

*/

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

//        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }

}
