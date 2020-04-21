package tonnysunm.com.acornote

import android.app.Application
import tonnysunm.com.acornote.widget.ScreenWidget

/**
 * TODO: lack of noteId for NoteLabel when creating new note
 */

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        val receiver = ScreenWidget()
        registerReceiver(receiver, receiver.getFilter())
    }
}