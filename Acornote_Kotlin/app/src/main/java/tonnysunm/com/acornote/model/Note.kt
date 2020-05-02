package tonnysunm.com.acornote.model

import androidx.room.*
import java.util.*

@Entity(
    tableName = "note_table",
    indices = [
        Index(value = ["color_tag_color"])
    ]
)
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(index = true)
    var order: Int,

    var title: String,

    var description: String? = null,

    var star: Boolean? = null,

    var pinned: Boolean? = null,

    @SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
    @Embedded(prefix = "color_tag_")
    var colorTag: ColorTag? = null,

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

data class NoteWithImageUrl(
    @Embedded
    val note: Note,

    var imageUrl: String?
)

data class NoteWithImages(
    @Embedded
    val note: Note,

    @Relation(parentColumn = "id", entityColumn = "note_id")
    var images: List<Image>?
)

fun String.textAsTitle() = split(" ").size <= 3