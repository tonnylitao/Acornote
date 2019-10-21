package tonnysunm.com.acornote.model

import android.app.Application
import androidx.lifecycle.LiveData

class Repository(private val application: Application) {

    private val folderDao: FolderDao by lazy { AppRoomDatabase.getDatabase(application).folderDao() }
    private val itemDao: ItemDao by lazy { AppRoomDatabase.getDatabase(application).itemDao() }

    // Folder
    val allFolders: LiveData<List<Folder>> = folderDao.getFolders()

    suspend fun insert(word: Folder) {
        folderDao.insert(word)
    }

    // Item
    fun allItems(folderTitle: String): LiveData<List<Item>> = itemDao.getItems(folderTitle)
}