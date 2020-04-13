package tonnysunm.com.acornote.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

interface SQLEntity

@Entity(
    tableName = "note_table"
)
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    @ColumnInfo(index = true)
    var order: Long,

    var title: String,

    var description: String? = null,

    var star: Boolean? = null,

    var pinned: Boolean? = null,

    @ColumnInfo(name = "color_tag_id", index = true)
    var colorTagId: Long? = null,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = Date().time,

    @ColumnInfo(name = "updated_at")
    var updatedAt: Long = Date().time
) : SQLEntity
