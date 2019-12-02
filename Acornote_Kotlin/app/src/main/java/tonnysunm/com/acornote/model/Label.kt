package tonnysunm.com.acornote.model

import androidx.room.*
import java.util.*

@Entity(tableName = "label_table", indices = [Index(value = ["title"], unique = true)])
data class Label(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    var title: String,

    var favourite: Boolean = false,

    var flippable: Boolean = false,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = Date().time,

    @ColumnInfo(name = "updated_at")
    var updatedAt: Long = Date().time
) : SQLEntity

data class LabelWrapper(
    @Embedded
    val label: Label,

    val noteCount: Int
)

const val EmptyId: Long = 0