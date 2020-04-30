package tonnysunm.com.acornote.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.full.primaryConstructor

class AndroidViewModelFactory(private vararg val args: Any) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        modelClass.kotlin.primaryConstructor?.call(*args)
            ?: throw IllegalArgumentException(
                "$modelClass primaryConstructor is null"
            )
}

