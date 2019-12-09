package tonnysunm.com.acornote.ui.label

import android.app.Application
import androidx.lifecycle.*
import tonnysunm.com.acornote.model.Label
import tonnysunm.com.acornote.model.Repository
import java.lang.IllegalStateException

class EditLabelViewModelFactory(
    private val application: Application,
    private val labelId: Long?
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        EditLabelViewModel(application, labelId) as T

}

class EditLabelViewModel(application: Application, private val labelId: Long?) :
    AndroidViewModel(application) {

    private val repository: Repository by lazy { Repository(application) }

    val labelLiveData: LiveData<Label> by lazy {
        repository.getLabel(labelId)
    }

    val labelEditing = LabelEditing()

    suspend fun updateOrInsertLabel(title: String): Long {
        val label = labelLiveData.value ?: throw IllegalStateException("label is not set")

        label.title = title

        return if (labelId == null) {
            repository.insert(label)
        } else {
            label.id = labelId
            repository.update(label)
        }
    }

    inner class LabelEditing {
        val title = MutableLiveData<String>()

        val flippable = MutableLiveData<Boolean>()
    }
}
