package tonnysunm.com.acornote.model

import android.content.Context
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.preference.PreferenceManager

class Repository(private val application: Context) {

    val labelDao by lazy { AppRoomDatabase.getDatabase(application).labelDao() }
    val noteDao by lazy { AppRoomDatabase.getDatabase(application).noteDao() }
    val noteLabelDao by lazy { AppRoomDatabase.getDatabase(application).noteLabelDao() }
    val colorTagDao by lazy { AppRoomDatabase.getDatabase(application).colorTagDao() }

    // Label
    val labels = labelDao.getLabelsWithNoteCount()

    fun getLabel(id: Long?): LiveData<Label> {
        if (id != null) {
            return labelDao.getLabel(id)
        }

        return MutableLiveData(Label(title = ""))
    }

    // Note
    fun notes(filter: NoteFilter): DataSource.Factory<Int, Note> = when (filter) {
        is NoteFilter.All -> {
            Log.d("ROOM", "get all")
            noteDao.getPagingAll()
        }
        is NoteFilter.Star -> {
            Log.d("ROOM", "get star")
            noteDao.getStar()
        }
        is NoteFilter.ByLabel -> {
            Log.d("ROOM", "get notes by label " + filter.id)
            noteDao.getByLabel(filter.id)
        }
        is NoteFilter.ByColorTag -> {
            Log.d("ROOM", "get notes by colortag  " + filter.colorTag.id)
            noteDao.getByColorTag(filter.colorTag.id)
        }
    }

//    private fun showOverlayIfNecessary() {
//        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
//        val overlayEnabled = prefs.getBoolean("settings_overlay", false)
//
//        if (overlayEnabled && Settings.canDrawOverlays(this)) {
//            startService(Intent(this, BubbleService::class.java))
//        }
//    }
}

