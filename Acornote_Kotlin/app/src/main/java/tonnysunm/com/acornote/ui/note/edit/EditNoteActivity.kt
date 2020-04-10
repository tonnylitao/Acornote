package tonnysunm.com.acornote.ui.note.edit

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import tonnysunm.com.acornote.R

class EditNoteActivity : AppCompatActivity(R.layout.activity_edit_note) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //
        val toolbar: Toolbar = findViewById(R.id.toolbar)
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
            supportFragmentManager.findFragmentById(R.id.fragment_edit_note) as? EditNoteFragment
        fragment?.insertOrUpdateNote()
    }
}
