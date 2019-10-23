package tonnysunm.com.acornote.model

import androidx.room.*

@Entity(tableName = "folder_table")
data class Folder(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
): SQLEntity

data class FolderWrapper(
    @Embedded
    val folder: Folder,

    val itemCount: Int
)
