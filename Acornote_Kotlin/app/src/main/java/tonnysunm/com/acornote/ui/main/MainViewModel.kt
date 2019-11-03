package tonnysunm.com.acornote.ui.main

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.*
import tonnysunm.com.acornote.model.*
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository by lazy { Repository(application) }

    val data: LiveData<List<FolderWrapper>> by lazy {
        repository.allFolders
    }

    override fun onCleared() {
        super.onCleared()

        viewModelScope.cancel()
    }


}
