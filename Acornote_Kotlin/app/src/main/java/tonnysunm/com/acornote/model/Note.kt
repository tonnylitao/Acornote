package tonnysunm.com.acornote.model

import androidx.room.*
import java.util.*

@Fts4
@Entity(
    tableName = "note_table"
)
data class Note(
    @ColumnInfo(name = "rowid")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(index = true)
    var order: Long,

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

    var editing: Boolean?
) {

    val hasDescription: Boolean
        get() {
            return description != null && description!!.trim().isNotEmpty()
        }
}

data class NoteWrapper(
    @Embedded
    val note: Note,

    @Relation(parentColumn = "rowid", entityColumn = "note_id")
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