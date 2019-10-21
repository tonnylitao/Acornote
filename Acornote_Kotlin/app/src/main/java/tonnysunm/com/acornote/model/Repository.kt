package tonnysunm.com.acornote.model

import androidx.lifecycle.LiveData

class Repository(private val folderDao: FolderDao) {

    val allFolders: LiveData<List<Folder>> = folderDao.getFolders()

    suspend fun insert(word: Folder) {
        folderDao.insert(word)
    }
}