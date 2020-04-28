package tonnysunm.com.acornote.model

import androidx.room.*
import java.util.*

@Entity(
    tableName = "image_table",
    foreignKeys = [ForeignKey(
        onDelete = ForeignKey.CASCADE,
        entity = Note::class,
        parentColumns = ["rowid"],
        childColumns = ["note_id"]
    )],
    indices = [Index(
        value = ["note_id"]
    )]
)
data class Image(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var url: String,

    @ColumnInfo(name = "note_id")
    var noteId: Int,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = Date().time
)
