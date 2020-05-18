package tonnysunm.com.acornote.ui.label

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.findFragment
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import tonnysunm.com.acornote.databinding.ListItemLabelBinding
import tonnysunm.com.acornote.model.LabelWithChecked

class LabelListAdapter :
    PagedListAdapter<LabelWithChecked, LabelListAdapter.ViewHolder>(LabelWithChecked.DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ListItemLabelBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ).apply {
                root.setOnClickListener {
                    val item = data ?: return@setOnClickListener

                    val fragment = it.findFragment<LabelListFragment>()
                    fragment.viewModel.flipChecked(item)
                }
            }
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) ?: return

        holder.binding.data = item
        holder.binding.executePendingBindings()
    }

    /* ViewHolder */

    inner class ViewHolder(val binding: ListItemLabelBinding) :
        RecyclerView.ViewHolder(binding.root)
}