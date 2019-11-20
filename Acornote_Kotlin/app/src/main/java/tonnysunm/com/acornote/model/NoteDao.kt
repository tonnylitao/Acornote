package tonnysunm.com.acornote.model

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*


@Dao
interface NoteDao {

    // Room executes all queries on a separate thread. So there is no suspend.
    @Query("SELECT * from note_table WHERE folder_id = (:folderId) ORDER BY updated_at DESC")
    fun getNotes(folderId: Long): DataSource.Factory<Int, Note>

    @Query("SELECT * from note_table ORDER BY updated_at DESC")
    fun getAllNotes(): DataSource.Factory<Int, Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: Note): Long

    @Update
    suspend fun update(note: Note)

    @Query("SELECT * from note_table WHERE id = :id LIMIT 1")
    fun note(id: Long): LiveData<Note>
}