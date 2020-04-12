package tonnysunm.com.acornote.ui.label

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.PagedList
import androidx.paging.toLiveData
import tonnysunm.com.acornote.model.NoteLabel
import tonnysunm.com.acornote.model.Repository

class EditLabelViewModelFactory(
    private val application: Application
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        EditLabelViewModel(application) as T
}

class EditLabelViewModel(application: Application) :
    AndroidViewModel(application) {

    private val repository: Repository by lazy { Repository(application) }

    val data: LiveData<PagedList<NoteLabel>> by lazy {
        repository.noteLabelDao.getAll().toLiveData(pageSize = 5)
    }

}
