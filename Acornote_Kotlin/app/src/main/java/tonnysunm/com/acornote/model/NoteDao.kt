package tonnysunm.com.acornote.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlin.math.max
import kotlin.math.min


@Dao
interface NoteDao : BaseDao<Note> {

    // Room executes all queries on a separate thread. So there is no suspend.
    @Query("SELECT a.*, b.url as imageUrl FROM note_table a LEFT JOIN image_table b ON b.id = (SELECT id FROM image_table WHERE note_id = a.id ORDER BY id ASC LIMIT 1) WHERE a.editing = 0 GROUP BY a.id ORDER BY a.`order` DESC, a.id DESC")
    fun getPagingAll(): DataSource.Factory<Int, NoteWithImageUrl>

    @Query("SELECT a.*, b.url as imageUrl from note_table a LEFT JOIN image_table b ON b.id = (SELECT id FROM image_table WHERE note_id = a.id ORDER BY id ASC LIMIT 1) WHERE a.editing = 0 AND a.star == 1 GROUP BY a.id ORDER BY a.pinned DESC, a.`order` DESC, a.id DESC")
    fun getStar(): DataSource.Factory<Int, NoteWithImageUrl>

    @Query("SELECT a.*, b.url as imageUrl from note_table a LEFT JOIN image_table b ON b.id = (SELECT id FROM image_table WHERE note_id = a.id ORDER BY id ASC LIMIT 1) WHERE editing = 0 AND color_tag_color = :color GROUP BY a.id ORDER BY pinned DESC, `order` DESC, id DESC")
    fun getByColorTag(color: String): DataSource.Factory<Int, NoteWithImageUrl>

    @Query("SELECT a.*, c.url as imageUrl from note_table a INNER JOIN note_label_table b ON a.id = b.note_id LEFT JOIN image_table c ON c.id = (SELECT id FROM image_table WHERE note_id = a.id ORDER BY id ASC LIMIT 1) WHERE a.editing = 0 AND b.label_id = :id GROUP BY a.id ORDER BY a.pinned DESC, a.`order` DESC, a.id DESC")
    fun getByLabel(id: Int): DataSource.Factory<Int, NoteWithImageUrl>

    //
    @Query("SELECT * from note_table WHERE editing = 0")
    suspend fun getAll(): List<Note>

    @Query("SELECT * from note_table WHERE editing = 0 AND title NOT LIKE 'http%' ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandom(): Note?

    @Query("SELECT count(*) from note_table WHERE title = :title")
    suspend fun getCountByString(title: String): Int

    @Update
    suspend fun updateNotes(notes: MutableList<Note>): Int

//    @Query("SELECT * from note_table WHERE id = :id")
//    fun note(id: Int): LiveData<Note>

    @Transaction
    @Query("SELECT * from note_table WHERE id = :id")
    fun noteWithImages(id: Int): LiveData<NoteWithImages?>

    @Transaction
    @Query("SELECT * from note_table WHERE editing = 1 LIMIT 1")
    fun noteEditingWithImages(): LiveData<NoteWithImages?>

    @Query("SELECT count(*) from note_table WHERE editing = 0")
    fun notesAllCount(): LiveData<Int>

    @Query("SELECT count(*) from note_table WHERE editing = 0")
    suspend fun notesCount(): Int

    @Query("SELECT MAX(`order`) from note_table WHERE editing = 0")
    suspend fun maxOrder(): Int?

    @Query("SELECT count(*) from note_table WHERE editing = 0 AND star == 1")
    fun notesStarCount(): LiveData<Int>

    @Query("UPDATE note_table set `order` = `order` + :delta WHERE editing = 0 AND id = :id")
    suspend fun updateOrder(id: Int, delta: Int)

    @Query(
        "UPDATE note_table set `order` = `order` + :delta WHERE editing = 0 AND `order` >= :min AND `order` <= :max AND id != :target"
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

    val colorTagColor: String?
        get() = when (this) {
            is ByColorTag -> this.colorTag.color
            else -> null
        }
}
