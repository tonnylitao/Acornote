package tonnysunm.com.acornote.ui.note.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.paging.PagedList
import androidx.paging.toLiveData
import tonnysunm.com.acornote.model.Note
import tonnysunm.com.acornote.model.NoteFilter
import tonnysunm.com.acornote.model.NoteWithImageUrl
import tonnysunm.com.acornote.model.Repository

class NoteListViewModel(application: Application) :
    AndroidViewModel(application) {

    private val _repository: Repository by lazy { Repository(application) }

    private val _noteFilterLiveData: MutableLiveData<NoteFilter> = MutableLiveData(NoteFilter.All)

    val data: LiveData<PagedList<NoteWithImageUrl>> = _noteFilterLiveData.switchMap {
        _repository.notes(it).toLiveData(pageSize = 5)
    }

    fun setFilter(filter: NoteFilter) {
        _noteFilterLiveData.value = filter
    }

    suspend fun updateNotes(list: MutableList<Note>) = _repository.noteDao.updateNotes(list)
}
