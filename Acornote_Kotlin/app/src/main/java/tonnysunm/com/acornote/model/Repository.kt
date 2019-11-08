package tonnysunm.com.acornote.model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

class Repository(private val application: Application) {

    private val folderDao: FolderDao by lazy {
        AppRoomDatabase.getDatabase(application).folderDao()
    }
    private val itemDao: ItemDao by lazy { AppRoomDatabase.getDatabase(application).itemDao() }

    suspend fun <T : SQLEntity> insert(entity: T): Long {
        if (entity is Folder) {
            return folderDao.insert(entity)
        } else if (entity is Item) {
            return itemDao.insert(entity)
        }


        throw IllegalArgumentException("type is not right.")
    }

    suspend fun <T : SQLEntity> update(entity: T, updatedAt: Long = Date().time) {
        if (entity is Folder) {
            entity.updatedAt = updatedAt
            folderDao.update(entity)
        } else if (entity is Item) {
            entity.updatedAt = updatedAt
            itemDao.update(entity)
        }

    }

    // Folder
    val allFolders = folderDao.getFolders()

    fun getFolder(id: Long?): LiveData<Folder> {
        if (id != null) {
            val liveData = folderDao.folder(id)
            if (liveData.value != null) {
                return liveData
            }
        }

        return MutableLiveData<Folder>().apply {
            value = Folder(title = "")
        }
    }


    // Item
    fun items(folderTitle: Long) = itemDao.getItems(folderTitle)

    fun getItem(id: Long?, folderId: Long): LiveData<Item> {
        if (id != null) {
            val liveData = itemDao.item(id)
            if (liveData.value != null) {
                return liveData
            }
        }

        return MutableLiveData<Item>().apply {
            value = Item(title = "", folderId = folderId)
        }
    }
}