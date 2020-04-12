package tonnysunm.com.acornote.model

import androidx.room.*
import java.util.*

@Entity(
    tableName = "note_label_table",
    foreignKeys = [ForeignKey(
        entity = Label::class,
        parentColumns = ["id"],
        childColumns = ["label_id"]
    ), ForeignKey(
        entity = Note::class,
        parentColumns = ["id"],
        childColumns = ["note_id"]
    )],
    indices = [Index(
        value = ["label_id", "note_id"],
        unique = true
    )]
)
data class NoteLabel(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    @ColumnInfo(name = "label_id", index = true)
    var labelId: Long,

    @ColumnInfo(name = "note_id", index = true)
    var noteId: Long,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = Date().time
) : SQLEntity