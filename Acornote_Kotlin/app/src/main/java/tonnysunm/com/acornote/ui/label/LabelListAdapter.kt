package tonnysunm.com.acornote.ui.label

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.findFragment
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tonnysunm.com.acornote.HomeActivity
import tonnysunm.com.acornote.databinding.ListItemLabelBinding
import tonnysunm.com.acornote.model.NoteLabel

class LabelListAdapter :
    PagedListAdapter<NoteLabel, LabelListAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ListItemLabelBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    public override fun getItem(position: Int) = super.getItem(position)

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<NoteLabel>() {
            override fun areItemsTheSame(old: NoteLabel, aNew: NoteLabel): Boolean {
                return old.id == aNew.id
            }

            override fun areContentsTheSame(old: NoteLabel, aNew: NoteLabel): Boolean {
                return old == aNew
            }
        }
    }


    /* ViewHolder */

    inner class ViewHolder(private val binding: ListItemLabelBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.clickListener = View.OnClickListener {
                val note = binding.data ?: return@OnClickListener

                val activity = it.findFragment<LabelListFragment>().activity as? HomeActivity
                    ?: return@OnClickListener


            }
        }

        fun bind(note: NoteLabel) {
            binding.data = note

            binding.executePendingBindings()
        }
    }

}