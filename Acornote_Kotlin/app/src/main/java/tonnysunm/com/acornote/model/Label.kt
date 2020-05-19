package tonnysunm.com.acornote.model

import androidx.room.*
import tonnysunm.com.acornote.ui.IDEquable

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
) : IDEquable {
    @Ignore
    override val id = label.id
}

const val EmptyId = 0