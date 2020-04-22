package tonnysunm.com.acornote.ui.drawer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import tonnysunm.com.acornote.model.LabelWithNoteCount
import tonnysunm.com.acornote.model.Repository

class DrawerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository by lazy { Repository(application) }

    val allNotesCountLiveData: LiveData<Int> by lazy {
        repository.noteDao.notesAllCount()
    }

    val starCountLiveData: LiveData<Int> by lazy {
        repository.noteDao.notesStarCount()
    }

    val data: LiveData<List<LabelWithNoteCount>> by lazy {
        repository.labels
    }

    override fun onCleared() {
        super.onCleared()

        viewModelScope.cancel()
    }
}
