package tonnysunm.com.acornote.ui.note

import android.app.Application
import android.icu.text.CaseMap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.paging.toLiveData
import tonnysunm.com.acornote.model.Folder
import tonnysunm.com.acornote.model.Note
import tonnysunm.com.acornote.model.NoteFilter
import tonnysunm.com.acornote.model.Repository


class DetailViewModelFactory(private val application: Application, private val filter: NoteFilter) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        NoteListViewModel(application, filter) as T

}

class NoteListViewModel(application: Application, filter: NoteFilter) :
    AndroidViewModel(application) {

    private val repository: Repository by lazy { Repository(application) }

//    val folder: LiveData<Folder>? by lazy {
//        when (filter) {
//            is NoteFilter.ByFolder -> repository.getFolder(filter.id)
//            else -> null
//        }
//    }

    val data: LiveData<PagedList<Note>> by lazy {
        repository.notes(filter).toLiveData(pageSize = 5)
    }
}
