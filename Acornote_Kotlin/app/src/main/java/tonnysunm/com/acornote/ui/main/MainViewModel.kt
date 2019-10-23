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

    val repository: Repository by lazy { Repository(application) }

    val data: LiveData<List<FolderWrapper>> by lazy {
        repository.allFolders
    }

//    init {
//        viewModelScope.launch {
//            val data = withContext(Dispatchers.IO) {
//                //api load data
//                delay(5000)
//            }
//
//            repository.insert(Folder(title = "Folder Title33", createdAt = Date().time, updatedAt = Date().time))
//            repository.insert(Item(folderId = 1, title = "Item Title112", createdAt = Date().time, updatedAt = Date().time))
//        }
//    }

    fun insert(folder: Folder) = viewModelScope.launch {
        repository.insert(folder)
    }

    override fun onCleared() {
        super.onCleared()

        viewModelScope.cancel()

    }


}
