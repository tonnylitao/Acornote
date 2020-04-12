package tonnysunm.com.acornote.ui.label

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tonnysunm.com.acornote.R

import kotlinx.android.synthetic.main.activity_edit_label.*

class LabelListActivity : AppCompatActivity(R.layout.activity_edit_label) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        return true
    }

}
