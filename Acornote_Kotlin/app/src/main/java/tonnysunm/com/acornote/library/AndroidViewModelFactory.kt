package tonnysunm.com.acornote.library

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/*
for Sealed Classes NoteFilter.All, parameter::class.java return NoteFilter$All, but expected NoteFilter
so this AndroidViewModelFactory does not support Sealed Classes.
 */

class AndroidViewModelFactory<P : Any>(
    private val application: Application,
    private val parameter: P
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        modelClass
            .getConstructor(Application::class.java, parameter::class.java)
            .newInstance(application, parameter)
}

class AndroidViewModelFactory2<P1 : Any, P2 : Any>(
    private val application: Application,
    private val parameter1: P1,
    private val parameter2: P2
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        modelClass
            .getConstructor(Application::class.java, parameter1::class.java, parameter2::class.java)
            .newInstance(application, parameter1, parameter2)
}

class AndroidViewModelFactory3<P1 : Any, P2 : Any, P3 : Any>(
    private val application: Application,
    private val parameter1: P1,
    private val parameter2: P2,
    private val parameter3: P3
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        modelClass
            .getConstructor(
                Application::class.java,
                parameter1::class.java,
                parameter2::class.java,
                parameter3::class.java
            )
            .newInstance(application, parameter1, parameter2, parameter3)
}