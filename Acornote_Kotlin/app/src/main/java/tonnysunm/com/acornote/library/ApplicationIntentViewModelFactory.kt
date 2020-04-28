package tonnysunm.com.acornote.library

import android.app.Application
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ApplicationIntentViewModelFactory(
    private val application: Application,
    private val intent: Intent
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        modelClass.getConstructor(Application::class.java, Intent::class.java)
            .newInstance(application, intent)
}
