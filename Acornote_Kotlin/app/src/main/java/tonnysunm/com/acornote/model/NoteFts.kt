package tonnysunm.com.acornote.model

import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.FtsOptions

@Fts4(contentEntity = Note::class, order = FtsOptions.Order.DESC)
@Entity(
    tableName = "note_fts_table"
)
data class NoteFts(
//    var rowid: Int = 0, optionally omitted

    var title: String,

    var description: String? = null
)