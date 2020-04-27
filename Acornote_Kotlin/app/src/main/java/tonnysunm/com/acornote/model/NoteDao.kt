package tonnysunm.com.acornote.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import kotlin.math.max
import kotlin.math.min


@Dao
interface NoteDao {
    //'[' || GROUP_CONCAT(b.url || '') || ']'
    // Room executes all queries on a separate thread. So there is no suspend.
    @Query("SELECT * FROM note_table WHERE editing = 0 ORDER BY `order` DESC, updated_at DESC")
    fun getPagingAll(): DataSource.Factory<Int, NoteWrapper>

    @Query("SELECT * from note_table WHERE editing = 0")
    fun getAll(): List<Note>

    @Query("SELECT * from note_table WHERE editing = 0 AND title NOT LIKE 'http%' ORDER BY RANDOM() LIMIT 1")
    fun getRandom(): Note?

    @Query("SELECT * from note_table WHERE editing = 0 AND star == 1 ORDER BY pinned DESC, `order` DESC, updated_at DESC")
    fun getStar(): DataSource.Factory<Int, NoteWrapper>

    @Query("SELECT * from note_table a INNER JOIN note_label_table b ON a.id = b.note_id WHERE a.editing = 0 AND b.label_id = :id ORDER BY a.pinned DESC, a.`order` DESC, a.updated_at DESC")
    fun getByLabel(id: Long): DataSource.Factory<Int, NoteWrapper>

    @Query("SELECT * from note_table WHERE editing = 0 AND color_tag_id = :id ORDER BY pinned DESC, `order` DESC, updated_at DESC")
    fun getByColorTag(id: Long): DataSource.Factory<Int, NoteWrapper>

    @Query("SELECT count(*) from note_table WHERE title = :title")
    fun getCountByString(title: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: Note): Long

    @Insert
    suspend fun insert(entities: List<Note>)

    @Update
    suspend fun update(note: Note)

    @Update
    suspend fun updateNotes(notes: MutableList<Note>): Int

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * from note_table WHERE id = :id LIMIT 1")
    fun note(id: Long): LiveData<Note>

    @Query("SELECT * from note_table WHERE editing = 1 ORDER BY id DESC LIMIT 1")
    fun noteEditing(): LiveData<Note?>

    @Query("SELECT count(*) from note_table WHERE editing = 0")
    fun notesAllCount(): LiveData<Int>

    @Query("SELECT count(*) from note_table WHERE editing = 0")
    fun notesCount(): Int

    @Query("SELECT MAX(`order`) from note_table WHERE editing = 0")
    fun maxOrder(): Long

    @Query("SELECT count(*) from note_table WHERE editing = 0 AND star == 1")
    fun notesStarCount(): LiveData<Int>

    @Query("UPDATE note_table set `order` = `order` + :delta WHERE editing = 0 AND id = :id")
    fun updateOrder(id: Long, delta: Long)

    @Query(
        "UPDATE note_table set `order` = `order` + :delta WHERE editing = 0 AND `order` >= :min AND `order` <= :max AND id != :target"
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
