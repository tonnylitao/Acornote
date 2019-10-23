package tonnysunm.com.acornote.model

import androidx.room.*

interface SQLEntity

@Entity(
    tableName = "item_table",
    foreignKeys = [ForeignKey(
        entity = Folder::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("folder_id")
    )]
)
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,

    val description: String,

    @ColumnInfo(name = "folder_id", index = true)
    val folderId: Int,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
): SQLEntity
