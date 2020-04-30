package tonnysunm.com.acornote.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import kotlin.math.max
import kotlin.math.min


@Dao
interface NoteDao : BaseDao<Note> {

    // Room executes all queries on a separate thread. So there is no suspend.
    @Transaction
    @Query("SELECT *, rowid FROM note_table WHERE editing = 0 ORDER BY `order` DESC, updated_at DESC")
    fun getPagingAll(): DataSource.Factory<Int, NoteWrapper>

    @Transaction
    @Query("SELECT *, rowid from note_table WHERE editing = 0 AND star == 1 ORDER BY pinned DESC, `order` DESC, updated_at DESC")
    fun getStar(): DataSource.Factory<Int, NoteWrapper>

    @Transaction
    @Query("SELECT a.*, a.rowid from note_table a INNER JOIN note_label_table b ON a.rowid = b.note_id WHERE a.editing = 0 AND b.label_id = :id ORDER BY a.pinned DESC, a.`order` DESC, a.updated_at DESC")
    fun getByLabel(id: Int): DataSource.Factory<Int, NoteWrapper>

    @Transaction
    @Query("SELECT *, rowid from note_table WHERE editing = 0 AND color_tag_id = :id ORDER BY pinned DESC, `order` DESC, updated_at DESC")
    fun getByColorTag(id: Int): DataSource.Factory<Int, NoteWrapper>

//    @Query("SELECT a.* FROM note_table a LEFT JOIN note_fts_table b ON a.id = b.rowid WHERE note_fts_table MATCH :query")
//    fun search(query: String?): LiveData<List<Note?>?>?

    //
    @Query("SELECT *, rowid from note_table WHERE editing = 0")
    suspend fun getAll(): List<Note>

    @Query("SELECT *, rowid from note_table WHERE editing = 0 AND title NOT LIKE 'http%' ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandom(): Note?

    @Query("SELECT count(*) from note_table WHERE title = :title")
    suspend fun getCountByString(title: String): Int

    @Insert
    suspend fun insert(entities: List<Note>)

    @Update
    suspend fun updateNotes(notes: MutableList<Note>): Int

    @Query("SELECT *, rowid from note_table WHERE rowid = :id LIMIT 1")
    fun note(id: Int): LiveData<Note>

    @Query("SELECT *, rowid from note_table WHERE editing = 1 ORDER BY rowid DESC LIMIT 1")
    fun noteEditing(): LiveData<Note?>

    @Query("SELECT count(*) from note_table WHERE editing = 0")
    fun notesAllCount(): LiveData<Int>

    @Query("SELECT count(*) from note_table WHERE editing = 0")
    suspend fun notesCount(): Int

    @Query("SELECT MAX(`order`) from note_table WHERE editing = 0")
    suspend fun maxOrder(): Int?

    @Query("SELECT count(*) from note_table WHERE editing = 0 AND star == 1")
    fun notesStarCount(): LiveData<Int>

    @Query("UPDATE note_table set `order` = `order` + :delta WHERE editing = 0 AND rowid = :id")
    suspend fun updateOrder(id: Int, delta: Int)

    @Query(
        "UPDATE note_table set `order` = `order` + :delta WHERE editing = 0 AND `order` >= :min AND `order` <= :max AND rowid != :target"
    )
    suspend fun moveNotes(target: Int, delta: Int, min: Int, max: Int)

    @Transaction
    suspend fun moveNote(target: Int, from: Int, to: Int) {
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

    data class ByLabel(val id: Int, val labelTitle: String) : NoteFilter(labelTitle)

    data class ByColorTag(val colorTag: ColorTag) : NoteFilter(colorTag.name)

    val labelId: Int?
        get() = when (this) {
            is ByLabel -> this.id
            else -> null
        }

    val colorTagId: Int?
        get() = when (this) {
            is ByColorTag -> this.colorTag.id
            else -> null
        }
}
