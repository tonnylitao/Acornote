package tonnysunm.com.acornote.ui.note.edit

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

class EditNoteViewModel(application: Application, private val id: Long?) :
    AndroidViewModel(application) {

    private val repository: Repository by lazy { Repository(application) }

    val data: LiveData<Note> by lazy {
        repository.getNote(id)
    }

    suspend fun updateOrInsertNote() {
        val note = data.value ?: throw IllegalStateException("note is not set")
        if (note.title.isEmpty()) throw IllegalStateException("title is null")

        if (id == null || id == 0.toLong()) {
            viewModelScope.launch(Dispatchers.IO) {
                note.order = repository.noteDao.maxOrder() + 1

                repository.insert(note)
            }
        } else {
            note.id = id
            repository.update(note)
        }
    }
}
