package tonnysunm.com.acornote.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource

class Repository(private val application: Application) {

    val labelDao: LabelDao by lazy {
        AppRoomDatabase.getDatabase(application).labelDao()
    }
    val noteDao: NoteDao by lazy { AppRoomDatabase.getDatabase(application).noteDao() }

    val noteLabelDao: NoteLabelDao by lazy {
        AppRoomDatabase.getDatabase(application).noteLabelDao()
    }

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
            noteDao.getAll()
        }
        is NoteFilter.Star -> {
            Log.d("ROOM", "get star")
            noteDao.getStar()
        }
        is NoteFilter.ByLabel -> {
            Log.d("ROOM", "get label by " + filter.id)
            noteDao.getByLabel(filter.id)
        }
    }

    fun getNote(id: Long?): LiveData<Note> = if (id != null && id > 0.toLong()) {
        noteDao.note(id)
    } else {
        MutableLiveData(
            Note(
                title = "",
                order = 0
            )
        )
    }
}

