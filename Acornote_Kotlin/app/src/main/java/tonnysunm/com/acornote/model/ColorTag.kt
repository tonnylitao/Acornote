package tonnysunm.com.acornote.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "color_tag_table", indices = [Index(value = ["color"], unique = true)])
data class ColorTag(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    var color: String,

    var name: String
) : SQLEntity