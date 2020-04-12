package tonnysunm.com.acornote.model

import androidx.paging.DataSource
import androidx.room.*

@Dao
interface NoteLabelDao {
    // Room executes all queries on a separate thread. So there is no suspend.
    @Query("SELECT * from note_label_table ORDER BY created_at DESC")
    fun getAll(): DataSource.Factory<Int, NoteLabel>

    @Delete
    suspend fun delete(entity: NoteLabel): Int

    @Insert
    suspend fun insert(entity: NoteLabel): Long
}