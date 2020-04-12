package tonnysunm.com.acornote.ui.label

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagedList
import androidx.paging.toLiveData
import kotlinx.coroutines.launch
import tonnysunm.com.acornote.model.Label
import tonnysunm.com.acornote.model.LabelWithCheckStatus
import tonnysunm.com.acornote.model.NoteLabel
import tonnysunm.com.acornote.model.Repository

class EditLabelViewModelFactory(
    private val application: Application,
    private val noteId: Long?
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        EditLabelViewModel(application, noteId) as T
}

private val TAG = "EditLabelViewModel"

class EditLabelViewModel(application: Application, noteId: Long?) :
    AndroidViewModel(application) {

    private val repository: Repository by lazy { Repository(application) }

    val data: LiveData<PagedList<LabelWithCheckStatus>> by lazy {
        if (noteId != null) {
            repository.labelDao.getLabelsWithNoteId(noteId).toLiveData(pageSize = 5)
        } else {
            repository.labelDao.getLabels().toLiveData(pageSize = 5)
        }
    }

    fun flipChecked(lwcs: LabelWithCheckStatus) {
        val labelId = lwcs.id
        val noteId = lwcs.noteId

        viewModelScope.launch {
            if (!lwcs.checked) {
                repository.noteLabelDao.insert(NoteLabel(labelId = labelId, noteId = noteId))
            } else {
                repository.noteLabelDao.delete(labelId, noteId)
            }
        }
    }

    fun createLabel(title: String) {
        viewModelScope.launch {
            repository.labelDao.insert(Label(title = title))
        }
    }
}
