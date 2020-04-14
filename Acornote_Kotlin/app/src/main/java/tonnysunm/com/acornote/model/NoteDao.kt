package tonnysunm.com.acornote.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import kotlin.math.max
import kotlin.math.min


@Dao
interface NoteDao {

    // Room executes all queries on a separate thread. So there is no suspend.
    @Query("SELECT * from note_table ORDER BY pinned DESC, `order` DESC, updated_at DESC")
    fun getAll(): DataSource.Factory<Int, Note>

    @Query("SELECT * from note_table  WHERE star == 1 ORDER BY pinned DESC, `order` DESC, updated_at DESC")
    fun getStar(): DataSource.Factory<Int, Note>

    @Query("SELECT a.* from note_table a INNER JOIN note_label_table b ON a.id = b.note_id WHERE b.label_id = :id ORDER BY a.pinned DESC, a.`order` DESC, a.updated_at DESC")
    fun getByLabel(id: Long): DataSource.Factory<Int, Note>

    @Query("SELECT * from note_table WHERE color_tag_id = :id ORDER BY pinned DESC, `order` DESC, updated_at DESC")
    fun getByColorTag(id: Long): DataSource.Factory<Int, Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: Note): Long

    @Update
    suspend fun update(note: Note)

    @Update
    suspend fun updateNotes(notes: MutableList<Note>): Int

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


sealed class NoteFilter(val title: String) {
    object All : NoteFilter("All")

    object Star : NoteFilter("Star")

    data class ByLabel(val id: Long, val labelTitle: String) : NoteFilter(labelTitle)

    data class ByColorTag(val colorTag: ColorTag) : NoteFilter(colorTag.name)

    val labelId: Long?
        get() = when (this) {
            is ByLabel -> this.id
            else -> null
        }

    val colorTagId: Long?
        get() = when (this) {
            is ByColorTag -> this.colorTag.id
            else -> null
        }
}
