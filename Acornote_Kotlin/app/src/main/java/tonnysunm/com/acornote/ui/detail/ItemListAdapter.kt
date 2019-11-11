package tonnysunm.com.acornote.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tonnysunm.com.acornote.databinding.ListItemBinding
import tonnysunm.com.acornote.model.Item


class ItemListAdapter :
    PagedListAdapter<Item, ItemListAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Item>() {

            override fun areItemsTheSame(old: Item, new: Item) = old.id == new.id

            override fun areContentsTheSame(old: Item, new: Item) = old == new
        }
    }

    /* ViewHolder */

    inner class ViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.setClickListener { view ->
                binding.data?.id?.let {

                }
            }
        }

        fun bind(item: Item) {
            binding.data = item
            binding.executePendingBindings()
        }
    }
}