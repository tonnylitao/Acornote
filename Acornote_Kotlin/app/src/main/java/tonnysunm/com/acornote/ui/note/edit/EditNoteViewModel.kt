package tonnysunm.com.acornote.ui.note

import android.app.Application
import androidx.lifecycle.*
import tonnysunm.com.acornote.model.Note
import tonnysunm.com.acornote.model.Repository
import java.lang.IllegalStateException


class EditNoteViewModelFactory(
    private val application: Application,
    private val id: Long?,
    private val folderId: Long?
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        EditNoteViewModel(application, id, folderId) as T
}

class EditNoteViewModel(application: Application, private val id: Long?, val folderId: Long?) :
    AndroidViewModel(application) {

    private val repository: Repository by lazy { Repository(application) }

    val noteLiveData: LiveData<Note> by lazy {
        repository.getNote(id, folderId = folderId)
    }

    suspend fun updateOrInsertNote(title: String, description: String) {
        val note = noteLiveData.value ?: throw IllegalStateException("note is not set")

        note.folderId = folderId
        note.title = title
        note.description = description

        if (id == null) {
            repository.insert(note)
        } else {
            note.id = id
            repository.update(note)
        }
    }
}
