package tonnysunm.com.acornote.model

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*


@Dao
interface NoteDao {

    // Room executes all queries on a separate thread. So there is no suspend.
    @Query("SELECT * from note_table ORDER BY updated_at DESC")
    fun getAll(): DataSource.Factory<Int, Note>

    @Query("SELECT * from note_table WHERE favourite == 1 ORDER BY updated_at DESC")
    fun getFavourite(): DataSource.Factory<Int, Note>

    @Query("SELECT * from note_table WHERE id == :id ORDER BY updated_at DESC")
    fun getByFolder(id: Long): DataSource.Factory<Int, Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: Note): Long

    @Update
    suspend fun update(note: Note)

    @Query("SELECT * from note_table WHERE id = :id LIMIT 1")
    fun note(id: Long): LiveData<Note>
}


sealed class NoteFilter {
    object All : NoteFilter()

    object Favourite : NoteFilter()
    
    data class ByFolder(val id: Long) : NoteFilter()
}
