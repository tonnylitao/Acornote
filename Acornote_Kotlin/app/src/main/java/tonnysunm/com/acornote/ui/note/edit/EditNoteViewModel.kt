package tonnysunm.com.acornote.ui.note.edit

import android.app.Application
import androidx.lifecycle.*
import tonnysunm.com.acornote.model.Note
import tonnysunm.com.acornote.model.Repository
import java.lang.IllegalStateException


class EditNoteViewModelFactory(
    private val application: Application,
    private val id: Long?
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        EditNoteViewModel(application, id) as T
}

class EditNoteViewModel(
    application: Application,
    private val id: Long?
) :
    AndroidViewModel(application) {

    private val repository: Repository by lazy { Repository(application) }

    val noteLiveData: LiveData<Note> by lazy {
        repository.getNote(id)
    }

    val noteEditing = NoteEditing()

    suspend fun updateOrInsertNote(
        labelId: Long,
        favourite: Boolean,
        title: String,
        description: String?
    ) {
        val note = noteLiveData.value ?: throw IllegalStateException("note is not set")

        note.labelId = if (labelId > 0) labelId else null

        note.title = title
        note.description = description
        note.favourite = favourite

        if (id == null) {
            repository.insert(note)
        } else {
            note.id = id
            repository.update(note)
        }
    }


    inner class NoteEditing {
        val title = MutableLiveData<String>()
        val description = MutableLiveData<String>()
    }
}
