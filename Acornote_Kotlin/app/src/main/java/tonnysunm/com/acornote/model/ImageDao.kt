package tonnysunm.com.acornote.model

import androidx.room.Dao
import androidx.room.Query


@Dao
interface ImageDao : BaseDao<Image> {
    @Query("SELECT * from image_table")
    fun getAll(): List<Image>
}