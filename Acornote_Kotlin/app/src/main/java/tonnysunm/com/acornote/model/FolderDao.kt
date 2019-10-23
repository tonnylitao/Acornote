package tonnysunm.com.acornote.model

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface FolderDao {

    // Room executes all queries on a separate thread. So there is no suspend.
    @Query("SELECT *, (select count(*) from item_table b where b.folder_id = a.id) as itemCount from folder_table a ORDER BY updated_at DESC")
    fun getFolders(): LiveData<List<FolderWrapper>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: Folder)
}