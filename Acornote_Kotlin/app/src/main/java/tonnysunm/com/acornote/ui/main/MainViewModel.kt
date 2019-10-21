package tonnysunm.com.acornote.ui.main

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.*
import tonnysunm.com.acornote.model.AppRoomDatabase
import tonnysunm.com.acornote.model.Folder
import tonnysunm.com.acornote.model.Item
import tonnysunm.com.acornote.model.Repository
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository by lazy {
        Repository(AppRoomDatabase.getDatabase(application).folderDao())
    }

    val data: LiveData<List<Folder>> by lazy {
        repository.allFolders
    }

//
//    init {
//        viewModelScope.launch {
//            val data = withContext(Dispatchers.IO) {
//                //api load data
//                delay(5000)
//
//                Folder(title = "Folder Title11", createdAt = Date().time, updatedAt = Date().time)
//            }
//
//            repository.insert(data)
//        }
//    }

    fun insert(word: Folder) = viewModelScope.launch {
        repository.insert(word)
    }

    override fun onCleared() {
        super.onCleared()

        viewModelScope.cancel()

    }


}
