package tonnysunm.com.acornote.model

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Update


@Dao
interface LabelDao {

    // Room executes all queries on a separate thread. So there is no suspend.
//    @Query("SELECT *, (select count(*) from note_label_table b where b.label_id = a.id) as noteCount from label_table a ORDER BY created_at DESC")
    @Query("SELECT a.id, a.title, count(b.id) AS noteCount FROM label_table a LEFT JOIN note_label_table b ON a.id = b.label_id GROUP BY a.id ORDER BY a.created_at DESC")
    fun getLabels(): LiveData<List<LabelWrapper>>


    @Query("SELECT * from label_table WHERE id = :id LIMIT 1")
    fun getLabel(id: Long): LiveData<Label>

    @Update
    suspend fun update(label: Label)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: Label): Long
}