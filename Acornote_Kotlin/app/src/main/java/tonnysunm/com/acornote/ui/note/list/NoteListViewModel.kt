package tonnysunm.com.acornote.ui.note

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
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

    val noteFilterLiveData: MutableLiveData<NoteFilter> by lazy {
        MutableLiveData(filter)
    }

//    val createVisibilityLiveData: LiveData<Int> = Transformations.map(noteFilterLiveData) {
//        return@map when (it) {
//            NoteFilter.All, is NoteFilter.ByFolder -> View.VISIBLE
//            else -> View.GONE
//        }
//    }

    val data: LiveData<PagedList<Note>> = Transformations.switchMap(noteFilterLiveData) {
        return@switchMap repository.notes(it).toLiveData(pageSize = 5)
    }
}
