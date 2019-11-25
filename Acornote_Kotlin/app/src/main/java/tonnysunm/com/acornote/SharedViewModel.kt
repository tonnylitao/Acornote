package tonnysunm.com.acornote

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tonnysunm.com.acornote.model.NoteFilter

class SharedViewModel : ViewModel() {

    val noteFilterLiveData: MutableLiveData<NoteFilter> by lazy {
        MutableLiveData<NoteFilter>().apply {
            value = NoteFilter.All
        }
    }

}
