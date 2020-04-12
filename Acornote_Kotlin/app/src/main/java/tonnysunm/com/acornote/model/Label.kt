package tonnysunm.com.acornote.model

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

data class LabelWrapper(
    val id: String,

    val title: String,

    val noteCount: Int
)

const val EmptyId: Long = 0