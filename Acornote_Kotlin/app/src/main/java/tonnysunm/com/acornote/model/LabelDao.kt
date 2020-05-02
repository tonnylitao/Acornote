package tonnysunm.com.acornote.model

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query


@Dao
interface LabelDao : BaseDao<Label> {

    // Room executes all queries on a separate thread. So there is no suspend.
    @Query("SELECT a.*, count(*) AS noteCount FROM label_table a LEFT JOIN note_label_table b ON a.id = b.label_id LEFT JOIN note_table c ON b.note_id = c.id AND c.editing = 0 GROUP BY a.id ORDER BY a.id DESC")
    fun getLabelsWithNoteCount(): LiveData<List<LabelWithNoteCount>>

    @Query("SELECT a.*, count(b.note_id) > 0 AS checked FROM label_table a LEFT JOIN note_label_table b ON a.id = b.label_id AND b.note_id = :noteId GROUP BY a.id ORDER BY a.id DESC")
    fun getLabelsWithNoteId(noteId: Int): DataSource.Factory<Int, LabelWithChecked>

//    @Query("SELECT id, title, 0 as checked, 0 as noteId FROM label_table ORDER BY created_at DESC")
//    fun getPagingAll(): DataSource.Factory<Int, LabelWithCheckStatus>

    @Query("SELECT * FROM label_table")
    suspend fun getAll(): List<Label>

    @Query("SELECT * from label_table WHERE id = :id LIMIT 1")
    fun getLabel(id: Int): LiveData<Label>
}