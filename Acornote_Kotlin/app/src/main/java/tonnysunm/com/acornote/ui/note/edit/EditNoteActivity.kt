package tonnysunm.com.acornote.ui.note.edit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import tonnysunm.com.acornote.R
import tonnysunm.com.acornote.ui.note.EditNoteFragment

class EditNoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_note)


//        val fragment = EditNoteFragment()
//        fragment.arguments = intent.extras
//
//        supportFragmentManager.beginTransaction().run {
//            add(R.id.fragment_edit_note, fragment)
//            commit()
//        }
    }
}
