package tonnysunm.com.acornote.model

import androidx.room.*
import java.util.*

interface SQLEntity

@Entity(
    tableName = "note_table",
    foreignKeys = [ForeignKey(
        entity = Label::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("label_id")
    )],
    indices = [Index(value = ["title"], unique = true)]
)
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    var title: String,

    var description: String? = null,

    @ColumnInfo(name = "label_id", index = true)
    var labelId: Long?,

    var star: Boolean = false,

    var pinned: Boolean = false,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = Date().time,

    @ColumnInfo(name = "updated_at")
    var updatedAt: Long = Date().time
) : SQLEntity
