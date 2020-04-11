package tonnysunm.com.acornote.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import java.util.*

class Repository(private val application: Application) {

    private val labelDao: LabelDao by lazy {
        AppRoomDatabase.getDatabase(application).labelDao()
    }
    val noteDao: NoteDao by lazy { AppRoomDatabase.getDatabase(application).noteDao() }

    suspend fun <T : SQLEntity> insert(entity: T): Long {
        Log.d("Repository", "insert $entity")

        if (entity is Label) {
            return labelDao.insert(entity)
        } else if (entity is Note) {
            return noteDao.insert(entity)
        }

        throw IllegalArgumentException("type is not right.")
    }

    suspend fun <T : SQLEntity> update(entity: T, updatedAt: Long = Date().time): Long {
        Log.d("Repository", "update $entity")

        if (entity is Label) {
            labelDao.update(entity)
        } else if (entity is Note) {
            entity.updatedAt = updatedAt
            noteDao.update(entity)
        }

        throw IllegalArgumentException("entity type is not right.")
    }

    // Label
    val labels = labelDao.getLabels()

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

