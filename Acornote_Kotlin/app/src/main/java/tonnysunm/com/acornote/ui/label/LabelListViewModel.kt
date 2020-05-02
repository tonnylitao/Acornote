package tonnysunm.com.acornote.ui.label

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.toLiveData
import kotlinx.coroutines.launch
import tonnysunm.com.acornote.model.Label
import tonnysunm.com.acornote.model.LabelWithChecked
import tonnysunm.com.acornote.model.NoteLabelCrossRef
import tonnysunm.com.acornote.model.Repository

private const val TAG = "EditLabelViewModel"

class EditLabelViewModel(app: Application, val noteId: Int) : AndroidViewModel(app) {

    private val repository: Repository by lazy { Repository(app) }

    val data: LiveData<PagedList<LabelWithChecked>> by lazy {
        repository.labelDao.getLabelsWithNoteId(noteId).toLiveData(pageSize = 5)
    }

    fun flipChecked(lwcs: LabelWithChecked) {
        val labelId = lwcs.label.id

        viewModelScope.launch {
            val sharedPref =
                getApplication<Application>().getSharedPreferences("acronote", Context.MODE_PRIVATE)

            if (!lwcs.checked) {
                sharedPref.edit().putInt("default_label_id", lwcs.label.id).apply()

                val id =
                    repository.noteLabelDao.insert(
                        NoteLabelCrossRef(
                            labelId = labelId,
                            noteId = noteId
                        )
                    )
                Log.d("TAG", "insert noteLabel $id")
            } else {
                sharedPref.edit().remove("default_label_id").apply()

                repository.noteLabelDao.delete(labelId, noteId)

                Log.d("TAG", "delete noteLabel")
            }
        }
    }

    fun createLabel(title: String) {
        viewModelScope.launch {
            repository.labelDao.insert(Label(title = title))
        }
    }
}
