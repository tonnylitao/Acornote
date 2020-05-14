package tonnysunm.com.acornote.ui.label

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.toLiveData
import kotlinx.coroutines.launch
import timber.log.Timber
import tonnysunm.com.acornote.model.Label
import tonnysunm.com.acornote.model.LabelWithChecked
import tonnysunm.com.acornote.model.NoteLabelCrossRef
import tonnysunm.com.acornote.model.Repository

private const val TAG = "EditLabelViewModel"

class EditLabelViewModel(app: Application, val noteId: Int) : AndroidViewModel(app) {

    private val _repository: Repository by lazy { Repository(app) }

    val data: LiveData<PagedList<LabelWithChecked>> by lazy {
        _repository.labelDao.getLabelsWithNoteId(noteId).toLiveData(pageSize = 5)
    }

    fun flipChecked(lwcs: LabelWithChecked) {
        val labelId = lwcs.label.id

        viewModelScope.launch {
            val sharedPref =
                getApplication<Application>().getSharedPreferences("acronote", Context.MODE_PRIVATE)

            if (!lwcs.checked) {
                sharedPref.edit {
                    putInt("default_label_id", lwcs.label.id)
                }

                val id =
                    _repository.noteLabelDao.insert(
                        NoteLabelCrossRef(
                            labelId = labelId,
                            noteId = noteId
                        )
                    )
                Timber.d("insert noteLabel $id")
            } else {
                sharedPref.edit {
                    remove("default_label_id")
                }

                _repository.noteLabelDao.delete(labelId, noteId)

                Timber.d("delete noteLabel")
            }
        }
    }

    fun createLabel(title: String) {
        viewModelScope.launch {
            _repository.labelDao.insert(Label(title = title))
        }
    }
}