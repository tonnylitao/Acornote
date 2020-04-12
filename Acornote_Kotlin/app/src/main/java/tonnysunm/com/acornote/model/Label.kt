package tonnysunm.com.acornote.model

import android.util.Log
import android.view.View
import androidx.room.*
import java.util.*

@Entity(tableName = "label_table", indices = [Index(value = ["title"], unique = true)])
data class Label(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    var title: String,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = Date().time
) : SQLEntity

data class LabelWithNoteCount(
    @Embedded
    val label: Label,

    val noteCount: Int
)

data class LabelWithCheckStatus(
    val id: Long,
    val title: String,

    var noteId: Long,

    var checked: Boolean
)


const val EmptyId: Long = 0