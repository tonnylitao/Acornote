package tonnysunm.com.acornote.ui.note

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.paging.toLiveData
import tonnysunm.com.acornote.model.Note
import tonnysunm.com.acornote.model.Repository


class DetailViewModelFactory(private val application: Application, private val where: String) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        NoteListViewModel(application, where) as T

}

class NoteListViewModel(application: Application, where: String) : AndroidViewModel(application) {

    private val repository: Repository by lazy { Repository(application) }

    val data: LiveData<PagedList<Note>> by lazy {
        repository.notes(where).toLiveData(pageSize = 5)
    }
}
