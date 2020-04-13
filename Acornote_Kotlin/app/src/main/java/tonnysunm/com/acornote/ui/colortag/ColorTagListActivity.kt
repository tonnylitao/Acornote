package tonnysunm.com.acornote.ui.colortag

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_labels.*
import tonnysunm.com.acornote.R

class ColorTagListActivity : AppCompatActivity(R.layout.activity_colortags) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        setResult(Activity.RESULT_CANCELED)
        finish()

        return true
    }

}
