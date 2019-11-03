package tonnysunm.com.acornote.model

import androidx.room.*
import java.util.*

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
    val id: Long = 0,

    val title: String,

    val description: String,

    @ColumnInfo(name = "folder_id", index = true)
    val folderId: Long,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = Date().time,

    @ColumnInfo(name = "updated_at")
    var updatedAt: Long = Date().time
): SQLEntity
