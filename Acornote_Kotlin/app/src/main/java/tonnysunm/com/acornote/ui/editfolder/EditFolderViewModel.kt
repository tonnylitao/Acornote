package tonnysunm.com.acornote.ui.detail

import android.app.Application
import androidx.lifecycle.*
import tonnysunm.com.acornote.model.Folder
import tonnysunm.com.acornote.model.Repository
import java.lang.IllegalStateException

class EditFolderViewModelFactory(
    private val application: Application,
    private val folderId: Long?
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        EditFolderViewModel(application, folderId) as T

}

class EditFolderViewModel(application: Application, private val folderId: Long?) :
    AndroidViewModel(application) {

    private val repository: Repository by lazy { Repository(application) }

    val folderLiveData: LiveData<Folder> by lazy {
        repository.getFolder(folderId)
    }

    suspend fun updateOrInsertFolder(title: String) {
        val folder = folderLiveData.value ?: throw IllegalStateException("folder is not set")

        folder.title = title

        if (folderId == null) {
            repository.insert(folder)
        } else {
            folder.id = folderId
            repository.update(folder)
        }
    }
}
