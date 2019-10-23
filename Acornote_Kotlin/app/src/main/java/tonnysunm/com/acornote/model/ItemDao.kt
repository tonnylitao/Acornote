package tonnysunm.com.acornote.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface ItemDao {

    // Room executes all queries on a separate thread. So there is no suspend.
    @Query("SELECT * from item_table WHERE folder_id = (:id) ORDER BY updated_at DESC")
    fun getItems(id: Int): LiveData<List<Item>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: Item)
}