package tonnysunm.com.acornote.model

import androidx.room.Dao
import androidx.room.Query


@Dao
interface ImageDao : BaseDao<Image> {
    @Query("SELECT * from image_table")
    suspend fun getAll(): List<Image>
}