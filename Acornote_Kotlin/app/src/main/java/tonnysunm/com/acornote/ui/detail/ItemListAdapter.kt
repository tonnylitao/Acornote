package tonnysunm.com.acornote.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tonnysunm.com.acornote.R
import tonnysunm.com.acornote.databinding.ListItemBinding
import tonnysunm.com.acornote.model.Item

class ItemListAdapter: RecyclerView.Adapter<ItemListAdapter.ViewHolder>() {

    private var dataSource = emptyList<Item>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    internal fun setDataSource(words: List<Item>) {
        this.dataSource = words
        notifyDataSetChanged()
    }

    override fun getItemCount() = dataSource.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSource[position])
    }

    /* ViewHolder */

    inner class ViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener { view ->
                binding.data?.let {

                }
            }
        }

        fun bind(item: Item) {
            binding.data = item
            binding.executePendingBindings()
        }
    }
}