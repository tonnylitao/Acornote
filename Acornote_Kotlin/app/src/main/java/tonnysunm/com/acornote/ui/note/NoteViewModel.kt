package tonnysunm.com.acornote.ui.note

import android.app.Application
import android.content.Intent
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
            MutableLiveData(
                Note(
                    title = "",
                    order = 0,
                    star = intent.getBooleanExtra("star", false)
                )
            )
        }
    }

    suspend fun insertNote(labelId: Long?) {
        val note = data.value ?: throw IllegalStateException("note is not set")
        if (note.title.trim().isEmpty()) throw IllegalStateException("title is null")

        viewModelScope.launch(Dispatchers.IO) {
            note.order = repository.noteDao.maxOrder() + 1

            val noteId = repository.noteDao.insert(note)

            if (labelId != null) {
                repository.noteLabelDao.insert(NoteLabel(noteId = noteId, labelId = labelId))
            }
        }
    }

    suspend fun updateNote() {
        val note = data.value ?: throw IllegalStateException("note is not set")
        if (note.title.isEmpty()) throw IllegalStateException("title is null")

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
