package tonnysunm.com.acornote.model

import androidx.room.*
import java.util.*

@Entity(tableName = "folder_table", indices = [Index(value = ["title"], unique = true)])
data class Folder(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    var title: String,

    var favourite: Boolean = false,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = Date().time,

    @ColumnInfo(name = "updated_at")
    var updatedAt: Long = Date().time
) : SQLEntity

data class FolderWrapper(
    @Embedded
    val folder: Folder,

    val noteCount: Int
)

const val EmptyId: Long = 0