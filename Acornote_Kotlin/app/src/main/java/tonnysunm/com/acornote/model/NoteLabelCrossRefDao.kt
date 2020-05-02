package tonnysunm.com.acornote.model

import androidx.room.Dao
import androidx.room.Query

@Dao
interface NoteLabelCrossRefDao : BaseDao<NoteLabelCrossRef> {
    // Room executes all queries on a separate thread. So there is no suspend.
//    @Query("SELECT * from note_label_table ORDER BY id DESC")
//    fun getPagingAll(): DataSource.Factory<Int, NoteLabel>

    @Query("SELECT * from note_label_table")
    suspend fun getAll(): List<NoteLabelCrossRef>

    @Query("DELETE FROM note_label_table WHERE label_id = :labelId AND note_id = :noteId")
    suspend fun delete(labelId: Int, noteId: Int)

}