package tonnysunm.com.acornote.ui.label

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.findFragment
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tonnysunm.com.acornote.databinding.ListItemLabelBinding
import tonnysunm.com.acornote.model.LabelWithChecked

class LabelListAdapter :
    PagedListAdapter<LabelWithChecked, LabelListAdapter.ViewHolder>(DiffCallback) {

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
        private val DiffCallback = object : DiffUtil.ItemCallback<LabelWithChecked>() {
            override fun areItemsTheSame(
                old: LabelWithChecked,
                aNew: LabelWithChecked
            ): Boolean {
                return old.label.id == aNew.label.id
            }

            override fun areContentsTheSame(
                old: LabelWithChecked,
                aNew: LabelWithChecked
            ): Boolean {
                return old == aNew
            }
        }
    }


    /* ViewHolder */

    inner class ViewHolder(private val binding: ListItemLabelBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val data = binding.data ?: return@setOnClickListener

                val fragment = it.findFragment<LabelListFragment>()
                fragment.viewModel.flipChecked(data)
            }
        }

        fun bind(item: LabelWithChecked) {
            binding.data = item

            binding.executePendingBindings()
        }
    }

}