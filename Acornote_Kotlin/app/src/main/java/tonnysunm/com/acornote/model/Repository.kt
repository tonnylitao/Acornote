package tonnysunm.com.acornote.model

import android.content.Context
import androidx.paging.DataSource
import timber.log.Timber

class Repository(private val application: Context) {

    val labelDao by lazy { AppRoomDatabase.getDatabase(application).labelDao() }
    val noteDao by lazy { AppRoomDatabase.getDatabase(application).noteDao() }
    val noteLabelDao by lazy { AppRoomDatabase.getDatabase(application).noteLabelDao() }
    val colorTagDao by lazy { AppRoomDatabase.getDatabase(application).colorTagDao() }
    val imageDao by lazy { AppRoomDatabase.getDatabase(application).imageDao() }

    // Label
    val labels = labelDao.getLabelsWithNoteCount()

    // Note
    fun notes(filter: NoteFilter): DataSource.Factory<Int, NoteWithImageUrl> = when (filter) {
        is NoteFilter.All -> {
            Timber.d("get all")
            noteDao.getPagingAll()
        }
        is NoteFilter.Star -> {
            Timber.d("get star")
            noteDao.getStar()
        }
        is NoteFilter.ByLabel -> {
            Timber.d("get notes by label " + filter.id)
            noteDao.getByLabel(filter.id)
        }
        is NoteFilter.ByColorTag -> {
            Timber.d("get notes by colortag  " + filter.colorTag.color)
            noteDao.getByColorTag(filter.colorTag.color)
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

