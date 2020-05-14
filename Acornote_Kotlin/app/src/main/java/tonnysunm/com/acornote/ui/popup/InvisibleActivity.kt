package tonnysunm.com.acornote.ui.popup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import tonnysunm.com.acornote.library.AndroidViewModelFactory
import tonnysunm.com.acornote.ui.note.NoteViewModel

class InvisibleActivity : AppCompatActivity() {

    private val viewModel by viewModels<NoteViewModel> {
        AndroidViewModelFactory(this.application, this.intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val text = intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT)?.trim()
        if (text == null || text.isEmpty()) {
            finish()
            return
        }

        super.onCreate(savedInstanceState)

        viewModel.createNote(text) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
