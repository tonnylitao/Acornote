package tonnysunm.com.acornote.model

import androidx.room.*
import java.util.*

@Entity(tableName = "label_table", indices = [Index(value = ["title"], unique = true)])
data class Label(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var title: String,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = Date().time
)

data class LabelWithNoteCount(
    @Embedded
    val label: Label,

    val noteCount: Int
)

data class LabelWithCheckStatus(
    val id: Int,
    val title: String,

    var noteId: Int,

    var checked: Boolean
)


const val EmptyId = 0