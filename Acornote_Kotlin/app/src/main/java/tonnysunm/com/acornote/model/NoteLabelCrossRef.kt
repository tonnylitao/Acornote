package tonnysunm.com.acornote.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = "note_label_table",
    primaryKeys = ["label_id", "note_id"],
    foreignKeys = [ForeignKey(
        onDelete = CASCADE,
        entity = Label::class,
        parentColumns = ["id"],
        childColumns = ["label_id"]
    ), ForeignKey(
        onDelete = CASCADE,
        entity = Note::class,
        parentColumns = ["id"],
        childColumns = ["note_id"]
    )]
)
data class NoteLabelCrossRef(
    @ColumnInfo(name = "label_id", index = true)
    var labelId: Int,

    @ColumnInfo(name = "note_id", index = true)
    var noteId: Int
)