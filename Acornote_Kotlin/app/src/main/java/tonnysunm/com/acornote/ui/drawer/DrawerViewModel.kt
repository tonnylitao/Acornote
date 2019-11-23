package tonnysunm.com.acornote.ui.drawer

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.*
import tonnysunm.com.acornote.model.*

class DrawerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository by lazy { Repository(application) }

    val data: LiveData<List<FolderWrapper>> by lazy {
        repository.folders
    }

    override fun onCleared() {
        super.onCleared()

        viewModelScope.cancel()
    }


}
