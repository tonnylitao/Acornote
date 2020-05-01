package tonnysunm.com.acornote.model

import androidx.room.*
import java.util.*

@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0, //keep Note.id == NoteFTS.rowid, Note.title == NoteFTS.title, Note.description == NoteFTS.description

    @ColumnInfo(index = true)
    var order: Int,

    var title: String,

    var description: String? = null,

    var star: Boolean? = null,

    var pinned: Boolean? = null,

    @ColumnInfo(name = "color_tag_id", index = true)
    var colorTagId: Int? = null,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = Date().time,

    @ColumnInfo(name = "updated_at")
    var updatedAt: Long = Date().time,

    var editing: Boolean = true
) {

    val hasDescription: Boolean
        get() {
            return description != null && description!!.trim().isNotEmpty()
        }
}

data class NoteWrapper(
    @Embedded
    val note: Note,

    @Relation(parentColumn = "id", entityColumn = "note_id")
    var imageUrls: List<Image>?
) {
    val hasImage: Boolean
        get() {
            return imageUrls != null && imageUrls!!.isNotEmpty()
        }

    val coverUrl: String?
        get() {
            return imageUrls?.firstOrNull()?.url
        }
}

fun String.textAsTitle() = split(" ").size <= 3