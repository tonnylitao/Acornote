package tonnysunm.com.acornote.model

import androidx.room.*
import java.util.*

@Entity(tableName = "folder_table")
data class Folder(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    var title: String,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = Date().time,

    @ColumnInfo(name = "updated_at")
    var updatedAt: Long = Date().time
): SQLEntity

data class FolderWrapper(
    @Embedded
    val folder: Folder,

    val itemCount: Int
)

const val EmptyId: Long = -1