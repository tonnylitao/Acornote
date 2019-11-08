package tonnysunm.com.acornote.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import tonnysunm.com.acornote.databinding.ListFolderBinding
import tonnysunm.com.acornote.model.FolderWrapper

class FolderListAdapter : RecyclerView.Adapter<FolderListAdapter.ViewHolder>() {

    private var dataSource = emptyList<FolderWrapper>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListFolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    internal fun setDataSource(words: List<FolderWrapper>) {
        this.dataSource = words
        notifyDataSetChanged()
    }

    override fun getItemCount() = dataSource.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSource[position])
    }


    /* ViewHolder */

    inner class ViewHolder(private val binding: ListFolderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener { view ->
                binding.data?.folder?.id?.let {
                    val action = MainFragmentDirections.actionMainFragmentToDetailFragment(it)
                    view.findNavController().navigate(action)
                }
            }
        }

        fun bind(item: FolderWrapper) {
            binding.data = item
            binding.executePendingBindings()
        }
    }
}