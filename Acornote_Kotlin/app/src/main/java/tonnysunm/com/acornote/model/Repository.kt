package tonnysunm.com.acornote.model

import android.app.Application
import androidx.lifecycle.LiveData

class Repository(private val application: Application) {

    private val folderDao: FolderDao by lazy { AppRoomDatabase.getDatabase(application).folderDao() }
    private val itemDao: ItemDao by lazy { AppRoomDatabase.getDatabase(application).itemDao() }

    // Folder
    val allFolders: LiveData<List<FolderWrapper>> = folderDao.getFolders()

    suspend fun <T: SQLEntity> insert(entity: T) {
        if (entity is Folder) {
            folderDao.insert(entity)
        }else if (entity is Item) {
            itemDao.insert(entity)
        }

    }
}