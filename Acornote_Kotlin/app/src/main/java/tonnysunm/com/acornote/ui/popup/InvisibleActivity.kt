package tonnysunm.com.acornote.ui.popup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tonnysunm.com.acornote.model.Note
import tonnysunm.com.acornote.model.NoteLabel
import tonnysunm.com.acornote.ui.note.EditNoteViewModelFactory
import tonnysunm.com.acornote.ui.note.NoteViewModel

class InvisibleActivity : AppCompatActivity() {

    val viewModel: NoteViewModel by viewModels {
        EditNoteViewModelFactory(this.application, this.intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val text = intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT)
        if (text == null || text.isEmpty()) {
            finish()
            return
        }

        super.onCreate(savedInstanceState)

        val activity = this
        GlobalScope.launch(Dispatchers.IO) {
            var tips = ""
            try {
                if (viewModel.repository.noteDao.getCountByString(text) > 0) {
                    tips = "Repeated"
                } else {
                    val order = viewModel.repository.noteDao.maxOrder() + 1

                    val note = Note(
                        title = text,
                        order = order,
                        editing = false
                    )

                    val newId = viewModel.repository.noteDao.insert(note)

                    val sharedPref =
                        application.getSharedPreferences("acronote", Context.MODE_PRIVATE)
                    val labelId = sharedPref.getLong("default_label_id", 0)

                    if (labelId > 0) {
                        viewModel.repository.noteLabelDao.insert(
                            NoteLabel(noteId = newId, labelId = labelId)
                        )
                    }
                    tips = "Save Success"
                }

            } catch (e: Exception) {
                Log.d("TAG", e.toString())
                tips = "Save Failed"
            } finally {
                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(activity, tips, Toast.LENGTH_SHORT).show()
                    activity.finish()
                }
            }
        }
    }
}
