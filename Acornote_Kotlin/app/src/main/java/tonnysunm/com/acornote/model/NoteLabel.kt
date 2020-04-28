package tonnysunm.com.acornote.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import java.util.*

@Entity(
    tableName = "note_label_table",
    foreignKeys = [ForeignKey(
        onDelete = CASCADE,
        entity = Label::class,
        parentColumns = ["id"],
        childColumns = ["label_id"]
    ), ForeignKey(
        onDelete = CASCADE,
        entity = Note::class,
        parentColumns = ["rowid"],
        childColumns = ["note_id"]
    )],
    indices = [Index(
        value = ["label_id", "note_id"],
        unique = true
    )]
)
data class NoteLabel(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "label_id", index = true)
    var labelId: Int,

    @ColumnInfo(name = "note_id", index = true)
    var noteId: Int,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = Date().time
)