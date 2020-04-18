package tonnysunm.com.acornote.ui.note

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tonnysunm.com.acornote.model.Note
import tonnysunm.com.acornote.model.NoteLabel
import tonnysunm.com.acornote.model.Repository
import java.util.*


class EditNoteViewModelFactory(
    private val application: Application,
    private val id: Long?
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        NoteViewModel(application, id) as T
}

private val TAG = "NoteViewModel"

class NoteViewModel(application: Application, private val id: Long?) :
    AndroidViewModel(application) {

    private val repository: Repository by lazy { Repository(application) }

    val data: LiveData<Note> by lazy {
        repository.getNote(id)
    }

    suspend fun insertNote(labelId: Long?) {
        val note = data.value ?: throw IllegalStateException("note is not set")
        if (note.title.trim().isEmpty()) throw IllegalStateException("title is null")
        if (id != null && id != 0L) throw IllegalStateException("id is not null")

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

        if (id != null) {
            note.colorTagId = colorTagId
            repository.noteDao.update(note)
        } else {
            note.colorTagId = colorTagId
            (data as? MutableLiveData<Note>)?.postValue(note)
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
