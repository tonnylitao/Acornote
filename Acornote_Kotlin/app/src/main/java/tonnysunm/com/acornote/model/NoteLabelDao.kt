package tonnysunm.com.acornote.model

import androidx.paging.DataSource
import androidx.room.*

@Dao
interface NoteLabelDao {
    // Room executes all queries on a separate thread. So there is no suspend.
    @Query("SELECT * from note_label_table ORDER BY created_at DESC")
    fun getAll(): DataSource.Factory<Int, NoteLabel>

    @Query("DELETE FROM note_label_table WHERE label_id = :labelId AND note_id = :noteId")
    suspend fun delete(labelId: Long, noteId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: NoteLabel): Long
}