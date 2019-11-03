package tonnysunm.com.acornote.model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

class Repository(private val application: Application) {

    private val folderDao: FolderDao by lazy { AppRoomDatabase.getDatabase(application).folderDao() }
    private val itemDao: ItemDao by lazy { AppRoomDatabase.getDatabase(application).itemDao() }

    suspend fun <T: SQLEntity> insert(entity: T): Long  {
        if (entity is Folder) {
            return folderDao.insert(entity)
        }else if (entity is Item) {
            return itemDao.insert(entity)
        }

        throw IllegalArgumentException("type is not right." )
    }

    suspend fun <T: SQLEntity> update(entity: T, updatedAt: Long = Date().time) {
        if (entity is Folder) {
            entity.updatedAt = updatedAt
            folderDao.update(entity)
        }else if (entity is Item) {
            entity.updatedAt = updatedAt
            itemDao.update(entity)
        }

    }

    // Folder
    val allFolders = folderDao.getFolders()

    fun folder(id: Long?): LiveData<Folder> {
        if (id != null) {
            return folderDao.folder(id)
        }

        val now = Date().time
        val liveData = MutableLiveData<Folder>()
        liveData.value = Folder(title = "", createdAt = now, updatedAt = now)
        return liveData
    }


    // Item
    fun items(folderTitle: Int) = itemDao.getItems(folderTitle)
}