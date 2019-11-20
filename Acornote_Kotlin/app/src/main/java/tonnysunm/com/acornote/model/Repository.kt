package tonnysunm.com.acornote.model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

class Repository(private val application: Application) {

    private val folderDao: FolderDao by lazy {
        AppRoomDatabase.getDatabase(application).folderDao()
    }
    private val noteDao: NoteDao by lazy { AppRoomDatabase.getDatabase(application).noteDao() }

    suspend fun <T : SQLEntity> insert(entity: T): Long {
        if (entity is Folder) {
            return folderDao.insert(entity)
        } else if (entity is Note) {
            return noteDao.insert(entity)
        }


        throw IllegalArgumentException("type is not right.")
    }

    suspend fun <T : SQLEntity> update(entity: T, updatedAt: Long = Date().time) {
        if (entity is Folder) {
            entity.updatedAt = updatedAt
            folderDao.update(entity)
        } else if (entity is Note) {
            entity.updatedAt = updatedAt
            noteDao.update(entity)
        }

    }

    // Folder
    val folders = folderDao.getFolders()

    fun getFolder(id: Long?): LiveData<Folder> {
        if (id != null) {
            val liveData = folderDao.getFolder(id)
            if (liveData.value != null) {
                return liveData
            }
        }

        return MutableLiveData<Folder>().apply {
            value = Folder(title = "")
        }
    }


    // Note
    fun notes(folderId: Long?) =
        if (folderId != null) noteDao.getNotes(folderId)
        else noteDao.getAllNotes()

    fun getNote(id: Long?, folderId: Long): LiveData<Note> {
        if (id != null) {
            val liveData = noteDao.note(id)
            if (liveData.value != null) {
                return liveData
            }
        }

        return MutableLiveData<Note>().apply {
            value = Note(title = "", folderId = folderId)
        }
    }
}