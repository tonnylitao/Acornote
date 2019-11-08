package tonnysunm.com.acornote.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import tonnysunm.com.acornote.model.Item
import tonnysunm.com.acornote.model.Repository
import java.lang.IllegalStateException


class EditItemViewModelFactory(
    private val application: Application,
    private val id: Long?,
    private val folderId: Long
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        EditItemViewModel(application, id, folderId) as T
}

class EditItemViewModel(application: Application, private val id: Long?, val folderId: Long) :
    AndroidViewModel(application) {

    private val repository: Repository by lazy { Repository(application) }

    val itemLiveData: LiveData<Item> by lazy {
        repository.getItem(id, folderId = folderId)
    }

    suspend fun updateOrInsertItem(title: String, description: String) {
        val item = itemLiveData.value ?: throw IllegalStateException("item is not set")

        item.folderId = folderId
        item.title = title
        item.description = description

        if (id == null) {
            repository.insert(item)
        } else {
            item.id = id
            repository.update(item)
        }
    }
}
