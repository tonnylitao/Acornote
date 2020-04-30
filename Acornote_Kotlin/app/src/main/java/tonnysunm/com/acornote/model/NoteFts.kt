package tonnysunm.com.acornote.model

import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Fts4(contentEntity = Note::class)
@Entity(
    tableName = "note_fts_table"
)
data class NoteFts(
    @PrimaryKey
    var rowid: Int = 0,

    var title: String,

    var description: String? = null
)