package tonnysunm.com.acornote.ui.colortag

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.findFragment
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.coroutines.launch
import tonnysunm.com.acornote.databinding.ListItemColortagHorizontalBinding
import tonnysunm.com.acornote.databinding.ListItemEditBinding
import tonnysunm.com.acornote.model.ColorTag
import tonnysunm.com.acornote.ui.HomeActivity
import tonnysunm.com.acornote.ui.note.NoteActivity
import tonnysunm.com.acornote.ui.note.NoteFragment

private const val ColorTagType = 0
private const val FooterType = 1


class ColorTagListAdapterHorizontal(
    var selectedColorTagColor: String?,
    var array: List<ColorTag>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        if (viewType == ColorTagType)
            ViewHolder(
                ListItemColortagHorizontalBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            ).apply {
                itemView.setOnClickListener {
                    val data = array[this.absoluteAdapterPosition]

                    val fragment = itemView.findFragment<ColorTagListFragmentHorizontal>()
                    val activity = fragment.activity
                    if (activity is HomeActivity) {
                        fragment.navigateToNotesBy(data)
                    } else if (activity is NoteActivity) {
                        val noteFgm = activity.fragment_edit_note as NoteFragment
                        val viewModal = noteFgm.viewModel
                        viewModal.viewModelScope.launch {
                            viewModal.updateColorTag(data)
                        }
                    }
                }
            }
        else
            EditViewHolder(
                ListItemEditBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            ).apply {
                itemView.setOnClickListener {
                    val fragment = it.findFragment<ColorTagListFragmentHorizontal>()
                    val activity = fragment.activity ?: return@setOnClickListener

                    val startForResult =
                        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                            }
                        }
                    startForResult.launch(Intent(activity, ColorTagListActivity::class.java))
                }
            }

    override fun getItemViewType(position: Int) =
        if (position < array.count()) ColorTagType else FooterType

    override fun getItemCount() = array.count() + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val item = array[position]

            with(holder.binding) {
                checked = item.color == selectedColorTagColor
                data = item
                executePendingBindings()
            }
        }
    }

    /* ViewHolder */

    inner class ViewHolder(val binding: ListItemColortagHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class EditViewHolder(binding: ListItemEditBinding) :
        RecyclerView.ViewHolder(binding.root)

}