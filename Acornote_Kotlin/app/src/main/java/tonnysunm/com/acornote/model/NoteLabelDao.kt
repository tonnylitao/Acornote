package tonnysunm.com.acornote.model

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NoteLabelDao : BaseDao<NoteLabel> {
    // Room executes all queries on a separate thread. So there is no suspend.
    @Query("SELECT * from note_label_table ORDER BY created_at DESC")
    fun getPagingAll(): DataSource.Factory<Int, NoteLabel>

    @Query("SELECT * from note_label_table")
    suspend fun getAll(): List<NoteLabel>

    @Query("DELETE FROM note_label_table WHERE label_id = :labelId AND note_id = :noteId")
    suspend fun delete(labelId: Long, noteId: Long)

    @Insert
    suspend fun insert(entities: List<NoteLabel>)
}