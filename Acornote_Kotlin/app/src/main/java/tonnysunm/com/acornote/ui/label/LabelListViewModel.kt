package tonnysunm.com.acornote.ui.label

import android.app.Application
import android.content.Context
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
    private val noteId: Long
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        EditLabelViewModel(application, noteId) as T
}

private val TAG = "EditLabelViewModel"

class EditLabelViewModel(app: Application, noteId: Long) :
    AndroidViewModel(app) {

    private val repository: Repository by lazy { Repository(app) }

    val data: LiveData<PagedList<LabelWithCheckStatus>> by lazy {
        repository.labelDao.getLabelsWithNoteId(noteId).toLiveData(pageSize = 5)
    }

    fun flipChecked(lwcs: LabelWithCheckStatus) {
        val labelId = lwcs.id
        val noteId = lwcs.noteId

        viewModelScope.launch {
            val sharedPref =
                getApplication<Application>().getSharedPreferences("acronote", Context.MODE_PRIVATE)

            if (!lwcs.checked) {
                sharedPref.edit().putLong("default_label_id", lwcs.id).apply()

                repository.noteLabelDao.insert(NoteLabel(labelId = labelId, noteId = noteId))
            } else {
                sharedPref.edit().remove("default_label_id").apply()

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
