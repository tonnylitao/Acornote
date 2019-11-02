package tonnysunm.com.acornote.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tonnysunm.com.acornote.model.Folder
import tonnysunm.com.acornote.model.Item
import tonnysunm.com.acornote.model.Repository


class EditFolderViewModelFactory (private val application: Application, private val folderId: Int?) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = EditFolderViewModel(application, folderId) as T

}

class EditFolderViewModel(application: Application, folderId: Int?) : AndroidViewModel(application) {

    private val repository: Repository by lazy { Repository(application) }

//    val = repository.folder(this)
}
