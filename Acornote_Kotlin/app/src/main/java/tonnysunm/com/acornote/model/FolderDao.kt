package tonnysunm.com.acornote.model

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Update


@Dao
interface FolderDao {

    // Room executes all queries on a separate thread. So there is no suspend.
    @Query("SELECT *, (select count(*) from item_table b where b.folder_id = a.id) as itemCount from folder_table a ORDER BY updated_at DESC")
    fun getFolders(): LiveData<List<FolderWrapper>>

    @Query("SELECT * from folder_table WHERE id = :id LIMIT 1")
    fun folder(id: Long): LiveData<Folder>

    @Update
    suspend fun update(folder: Folder)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: Folder): Long
}