package tonnysunm.com.acornote.ui.note

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import kotlinx.android.synthetic.main.activity_edit_note.*
import tonnysunm.com.acornote.R

class NoteActivity : AppCompatActivity(R.layout.activity_edit_note) {

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
