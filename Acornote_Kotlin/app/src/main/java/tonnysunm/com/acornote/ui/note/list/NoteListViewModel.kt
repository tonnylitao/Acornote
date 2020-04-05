package tonnysunm.com.acornote.ui.note.list

import android.app.Application
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.lifecycle.*
import androidx.paging.Config
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.toLiveData
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
//            NoteFilter.All, is NoteFilter.ByLabel -> View.VISIBLE
//            else -> View.GONE
//        }
//    }

    val data: LiveData<PagedList<Note>> = noteFilterLiveData.switchMap {
        repository.notes(it).toLiveData(pageSize = 5)
    }

    suspend fun reloadData(): LiveData<PagedList<Note>> {
        return repository.notes(noteFilterLiveData.value!!).toLiveData(pageSize = 5)
    }

    suspend fun moveItem(target: Long, from: Long, to: Long) =
        repository.noteDao.moveNote(target, from, to)

    suspend fun updateNotes(list: Set<Note>) = repository.noteDao.updateNotes(list)
}
