package tonnysunm.com.acornote.library

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/*
for Sealed Classes NoteFilter.All, parameter::class.java return NoteFilter$All, but expected NoteFilter
so parameterClass is for supporting Sealed Classes.
 */

class AndroidViewModelFactory<P : Any>(
    private val application: Application,
    private val parameter: P,
    private val parameterClass: Class<P>? = null
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        
        if (parameterClass == null && parameter::class.java.name.contains("$")) {
            throw IllegalArgumentException("parameterClass is must for Sealed Class ${parameter::class.java.name}")
        }

        modelClass
            .getConstructor(Application::class.java, parameterClass ?: parameter::class.java)
            .newInstance(application, parameter)
    }

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