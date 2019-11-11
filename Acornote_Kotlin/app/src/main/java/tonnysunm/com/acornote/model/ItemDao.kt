package tonnysunm.com.acornote.model

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*


@Dao
interface ItemDao {

    // Room executes all queries on a separate thread. So there is no suspend.
    @Query("SELECT * from item_table WHERE folder_id = (:folderId) ORDER BY updated_at DESC")
    fun getItems(folderId: Long): DataSource.Factory<Int, Item>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: Item): Long

    @Update
    suspend fun update(item: Item)

    @Query("SELECT * from item_table WHERE id = :id LIMIT 1")
    fun item(id: Long): LiveData<Item>
}