package tonnysunm.com.acornote.model

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface LabelDao : BaseDao<Label> {

    // Room executes all queries on a separate thread. So there is no suspend.
    @Query("SELECT a.*, count(b.id) AS noteCount FROM label_table a LEFT JOIN note_label_table b ON a.id = b.label_id GROUP BY a.id ORDER BY a.created_at DESC")
    fun getLabelsWithNoteCount(): LiveData<List<LabelWithNoteCount>>

    @Query("SELECT a.id, a.title, count(b.id) > 0 AS checked, :noteId AS noteId FROM label_table a LEFT JOIN note_label_table b ON a.id = b.label_id AND b.note_id = :noteId GROUP BY a.id ORDER BY a.created_at DESC")
    fun getLabelsWithNoteId(noteId: Int): DataSource.Factory<Int, LabelWithCheckStatus>

//    @Query("SELECT id, title, 0 as checked, 0 as noteId FROM label_table ORDER BY created_at DESC")
//    fun getPagingAll(): DataSource.Factory<Int, LabelWithCheckStatus>

    @Query("SELECT * FROM label_table")
    suspend fun getAll(): List<Label>

    @Query("SELECT * from label_table WHERE id = :id LIMIT 1")
    fun getLabel(id: Int): LiveData<Label>

    @Insert
    suspend fun insert(entities: List<Label>)
}