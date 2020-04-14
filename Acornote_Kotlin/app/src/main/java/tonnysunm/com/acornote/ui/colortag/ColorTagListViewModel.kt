package tonnysunm.com.acornote.ui.colortag

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tonnysunm.com.acornote.model.ColorTag
import tonnysunm.com.acornote.model.Repository

class ColorTagViewModelFactory(
    private val application: Application
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        ColorTagViewModel(application) as T
}

private val TAG = "ColorTagViewModel"

class ColorTagViewModel(application: Application) :
    AndroidViewModel(application) {

    private val repository: Repository by lazy { Repository(application) }

    val data: LiveData<List<ColorTag>> by lazy {
        repository.colorTagDao.getAll()
    }
}
