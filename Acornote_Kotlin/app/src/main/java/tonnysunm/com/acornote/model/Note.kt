package tonnysunm.com.acornote.model

import androidx.room.*
import java.util.*

interface SQLEntity

@Entity(
    tableName = "note_table",
    foreignKeys = [ForeignKey(
        entity = Folder::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("folder_id")
    )]
)
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    var title: String,

    var description: String? = null,

    @ColumnInfo(name = "folder_id", index = true)
    var folderId: Long?,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = Date().time,

    @ColumnInfo(name = "updated_at")
    var updatedAt: Long = Date().time
) : SQLEntity
