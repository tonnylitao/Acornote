package tonnysunm.com.acornote.ui.drawer

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.*
import tonnysunm.com.acornote.model.*

class DrawerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository by lazy { Repository(application) }

    val allNotesCountLiveData: LiveData<Int> by lazy {
        repository.notesAllCount()
    }

    val starCountLiveData: LiveData<Int> by lazy {
        repository.notesStarCount()
    }

    val data: LiveData<List<LabelWrapper>> by lazy {
        repository.labels
    }

    override fun onCleared() {
        super.onCleared()

        viewModelScope.cancel()
    }


}
