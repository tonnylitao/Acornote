package tonnysunm.com.acornote.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tonnysunm.com.acornote.model.NoteFilter

class HomeSharedViewModel : ViewModel() {

    private val _noteFilterLiveData = MutableLiveData<NoteFilter>(NoteFilter.All)

    val noteFilterLiveData: LiveData<NoteFilter>
        get() = _noteFilterLiveData

    fun setFilter(filter: NoteFilter) {
        _noteFilterLiveData.value = filter
    }
}
