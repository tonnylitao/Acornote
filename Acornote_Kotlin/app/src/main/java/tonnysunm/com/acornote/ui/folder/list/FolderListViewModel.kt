package tonnysunm.com.acornote.ui.folder

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagedList
import androidx.paging.toLiveData
import kotlinx.coroutines.*
import tonnysunm.com.acornote.model.*

class FolderListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository by lazy { Repository(application) }

    val data: LiveData<PagedList<FolderWrapper>> by lazy {
        Log.d("TAG", repository.folders.toString())
        
        repository.folders.toLiveData(pageSize = 5)
    }

    override fun onCleared() {
        super.onCleared()

        viewModelScope.cancel()
    }


}
