package tonnysunm.com.acornote.ui.colortag

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.invoke
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.findFragment
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.coroutines.Dispatchers
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
            )
        else
            EditViewHolder(
                ListItemEditBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

    override fun getItemViewType(position: Int) =
        if (position < array.count()) ColorTagType else FooterType

    override fun getItemCount() = array.count() + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(array[position])
        }
    }

    /* ViewHolder */

    inner class ViewHolder(private val binding: ListItemColortagHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val data = binding.data ?: return@setOnClickListener

                val fragment = it.findFragment<ColorTagListFragmentHorizontal>()
                val activity = fragment.activity
                if (activity is HomeActivity) {
                    fragment.navigateToNotesBy(data)
                } else if (activity is NoteActivity) {
                    val noteFgm = activity.fragment_edit_note as NoteFragment
                    val viewModal = noteFgm.viewModel
                    viewModal.viewModelScope.launch(Dispatchers.IO) {
                        viewModal.updateColorTag(data)
                    }
                }
            }
        }

        fun bind(item: ColorTag) {
            binding.checked = item.color == selectedColorTagColor

            binding.data = item
            binding.executePendingBindings()
        }
    }

    inner class EditViewHolder(binding: ListItemEditBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val fragment = it.findFragment<ColorTagListFragmentHorizontal>()
                val activity = fragment.activity ?: return@setOnClickListener

                val startForResult =
                    activity.prepareCall(ActivityResultContracts.StartActivityForResult()) {
                        if (it.resultCode == AppCompatActivity.RESULT_OK) {
                        }
                    }
                startForResult(Intent(activity, ColorTagListActivity::class.java))
            }
        }
    }

}