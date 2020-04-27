package tonnysunm.com.acornote.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface ImageDao {
    @Query("SELECT * from image_table")
    fun getAll(): List<Image>

    @Insert
    suspend fun insert(entity: Image): Long

    @Delete
    suspend fun delete(entity: Image): Int

}