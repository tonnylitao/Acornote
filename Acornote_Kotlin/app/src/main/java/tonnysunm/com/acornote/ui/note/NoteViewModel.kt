package tonnysunm.com.acornote.ui.note

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tonnysunm.com.acornote.model.EmptyId
import tonnysunm.com.acornote.model.Note
import tonnysunm.com.acornote.model.NoteLabel
import tonnysunm.com.acornote.model.Repository
import java.util.*


class EditNoteViewModelFactory(
    private val application: Application,
    private val intent: Intent
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        NoteViewModel(application, intent) as T
}

private val TAG = "NoteViewModel"

class NoteViewModel(application: Application, private val intent: Intent) :
    AndroidViewModel(application) {

    private val repository: Repository by lazy { Repository(application) }

    val isCreateNewNote: Boolean
        get() {
            val id = intent.getLongExtra("id", EmptyId)
            return id == EmptyId
        }

    val data: LiveData<Note> by lazy {
        val id = intent.getLongExtra("id", EmptyId)

        if (id > EmptyId) {
            repository.noteDao.note(id)
        } else {
            val text = intent.getStringExtra(Intent.EXTRA_TEXT)

            val textRemoveMediumLink = text?.let {
                val regex = Regex("^“(.*)” by  https://link.medium.com/")
                val match = regex.find(it)
                match?.groups?.last()?.value
            }

            val star = intent.getBooleanExtra("star", false)

            repository.noteDao.noteEditing().switchMap {
                if (it == null) {
                    val note = Note(
                        title = textRemoveMediumLink ?: "",
                        order = 0,
                        star = star,
                        editing = true
                    )

                    viewModelScope.launch(Dispatchers.IO) {
                        note.order = repository.noteDao.maxOrder() + 1

                        val newId = repository.noteDao.insert(note)
                        data.value?.id = newId

                        val labelId = intent.getLongExtra("labelId", 0)
                        if (labelId > 0) {
                            repository.noteLabelDao.insert(
                                NoteLabel(noteId = newId, labelId = labelId)
                            )
                        }

                        Log.d("TAG", "insert new note $newId")
                    }

                    MutableLiveData(note)
                } else {
                    Log.d("TAG", "find editing note $it")
                    if (!textRemoveMediumLink.isNullOrEmpty()) {
                        it.title = textRemoveMediumLink
                    }

                    MutableLiveData(it)
                }
            }
        }
    }

    suspend fun updateNote() {
        val note = data.value ?: throw IllegalStateException("note is not set")

        note.updatedAt = Date().time
        repository.noteDao.update(note)
    }

    suspend fun updateColorTag(colorTagId: Long) {
        val note = data.value ?: throw IllegalStateException("note is not set")

        if (isCreateNewNote) {
            note.colorTagId = colorTagId
            (data as? MutableLiveData<Note>)?.postValue(note)
        } else {
            note.colorTagId = colorTagId
            repository.noteDao.update(note)
        }
    }

    fun deleteNote() {
        data.value?.let {
            viewModelScope.launch(Dispatchers.IO) {
                repository.noteDao.delete(it)
            }
        }
    }
}
