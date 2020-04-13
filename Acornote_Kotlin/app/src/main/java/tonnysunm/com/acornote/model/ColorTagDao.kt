package tonnysunm.com.acornote.model

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface ColorTagDao {
    // Room executes all queries on a separate thread. So there is no suspend.
    @Query("SELECT * FROM color_tag_table ORDER BY id DESC")
    fun getAll(): DataSource.Factory<Int, ColorTag>

    @Query("SELECT count(*) from color_tag_table")
    fun notesCount(): Int

    @Insert
    fun insert(entities: List<ColorTag>)
}