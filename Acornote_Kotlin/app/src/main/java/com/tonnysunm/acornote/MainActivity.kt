package tonnysunm.com.acornote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

}
