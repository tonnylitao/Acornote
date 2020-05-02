package tonnysunm.com.acornote.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "label_table",
    indices = [
        Index(value = ["title"], unique = true)
    ]
)
data class Label(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var title: String
)

data class LabelWithNoteCount(
    @Embedded
    val label: Label,

    val noteCount: Int
)

data class LabelWithChecked(
    @Embedded
    val label: Label,

    var checked: Boolean
)

const val EmptyId = 0