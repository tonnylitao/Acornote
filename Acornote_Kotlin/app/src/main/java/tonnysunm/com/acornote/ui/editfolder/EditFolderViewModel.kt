package tonnysunm.com.acornote.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.*
import tonnysunm.com.acornote.model.Folder
import tonnysunm.com.acornote.model.Repository
import java.util.*


class EditFolderViewModelFactory (private val application: Application, private val folderId: Long?) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = EditFolderViewModel(application, folderId) as T

}

class EditFolderViewModel(application: Application, private val folderId: Long?) : AndroidViewModel(application) {

    private val repository: Repository by lazy { Repository(application) }

    val folderLiveData: LiveData<Folder> by lazy {
        repository.folder(folderId)
    }

    suspend fun updateOrInsertFolderAsync(title: String) = CoroutineScope(Dispatchers.IO).async {
        folderLiveData.value?.let {
            it.title = title

            if (folderId == null) {
                repository.insert(it)
            } else {
                it.id = folderId
                repository.update(it)
            }
        }
    }
}
