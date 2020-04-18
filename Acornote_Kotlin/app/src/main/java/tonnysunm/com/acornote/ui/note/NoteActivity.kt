package tonnysunm.com.acornote.ui.note

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_note.*
import tonnysunm.com.acornote.R

class NoteActivity : AppCompatActivity(R.layout.activity_note) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)

        //set drawer icon
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        insertOrUpdateNote()
        return true
    }

    override fun onBackPressed() {
        insertOrUpdateNote()
        super.onBackPressed()
    }

    private fun insertOrUpdateNote() {
        val fragment =
            supportFragmentManager.findFragmentById(R.id.fragment_edit_note) as? NoteFragment
        fragment?.insertOrUpdateNote()
    }
}
