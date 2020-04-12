package tonnysunm.com.acornote.ui.label

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tonnysunm.com.acornote.R

import kotlinx.android.synthetic.main.activity_edit_label.*
import tonnysunm.com.acornote.ui.note.NoteFragment

class LabelListActivity : AppCompatActivity(R.layout.activity_edit_label) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null
    }

    override fun onSupportNavigateUp(): Boolean {
        setResult(Activity.RESULT_CANCELED)
        finish()

        return true
    }

}
