package tonnysunm.com.acornote.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tonnysunm.com.acornote.model.NoteFilter

class HomeSharedViewModel : ViewModel() {

    val noteFilterLiveData by lazy {
        MutableLiveData<NoteFilter>(NoteFilter.All)
    }

}
