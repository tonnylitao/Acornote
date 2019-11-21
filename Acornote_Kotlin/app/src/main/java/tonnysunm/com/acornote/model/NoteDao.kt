package tonnysunm.com.acornote.model

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*


@Dao
interface NoteDao {

    // Room executes all queries on a separate thread. So there is no suspend.
    @Query("SELECT * from note_table WHERE :where ORDER BY updated_at DESC")
    fun getNotes(where: String): DataSource.Factory<Int, Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: Note): Long

    @Update
    suspend fun update(note: Note)

    @Query("SELECT * from note_table WHERE id = :id LIMIT 1")
    fun note(id: Long): LiveData<Note>
}