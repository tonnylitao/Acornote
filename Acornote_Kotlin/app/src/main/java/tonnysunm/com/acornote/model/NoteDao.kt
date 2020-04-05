package tonnysunm.com.acornote.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import java.util.*
import kotlin.math.min
import kotlin.math.max


@Dao
interface NoteDao {

    // Room executes all queries on a separate thread. So there is no suspend.
    @Query("SELECT * from note_table ORDER BY pinned DESC, `order` DESC, updated_at DESC")
    fun getAll(): DataSource.Factory<Int, Note>

    @Query("SELECT * from note_table  WHERE star == 1 ORDER BY pinned DESC, `order` DESC, updated_at DESC")
    fun getStar(): DataSource.Factory<Int, Note>

    @Query("SELECT * from note_table WHERE label_id == :id ORDER BY pinned DESC, `order` DESC, updated_at DESC")
    fun getByLabel(id: Long): DataSource.Factory<Int, Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: Note): Long

    @Update
    suspend fun update(note: Note)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNotes(notes: Set<Note>): Int

    @Query("SELECT * from note_table WHERE id = :id LIMIT 1")
    fun note(id: Long): LiveData<Note>

    @Query("SELECT count(*) from note_table")
    fun notesAllCount(): LiveData<Int>

    @Query("SELECT count(*) from note_table")
    fun notesCount(): Int

    @Query("SELECT MAX(`order`) from note_table")
    fun maxOrder(): Long

    @Query("SELECT count(*) from note_table WHERE star == 1")
    fun notesStarCount(): LiveData<Int>

    @Query("UPDATE note_table set `order` = `order` + :delta WHERE  id = :id")
    fun updateOrder(id: Long, delta: Long)

    @Query(
        "UPDATE note_table set `order` = `order` + :delta WHERE `order` >= :min and `order` <= :max and id != :target"
    )
    fun moveNotes(target: Long, delta: Long, min: Long, max: Long)

    @Transaction
    fun moveNote(target: Long, from: Long, to: Long) {
        Log.d("SQL", "moveNote $target $from $to")
        moveNotes(
            target,
            if (from > to) 1 else -1,
            min(from, to),
            max(from, to)
        )
        updateOrder(target, to - from)
    }
}


sealed class NoteFilter(val title: String, var loadAt: Date) {
    object All : NoteFilter("All", Date())

    object Star : NoteFilter("Star", Date())

    data class ByLabel(val id: Long, val labelTitle: String) : NoteFilter(
        labelTitle, Date()
    )

    fun reload(): NoteFilter {
        loadAt = Date()
        return this
    }

//    val title: String
//        get() = when (this) {
//            All -> "All"
//            Star -> "Star"
//            is ByLabel ->
//        }

    val labelId: Long?
        get() = when (this) {
            is ByLabel -> this.id
            else -> null
        }
}
