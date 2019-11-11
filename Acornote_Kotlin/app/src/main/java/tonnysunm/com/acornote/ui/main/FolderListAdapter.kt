package tonnysunm.com.acornote.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tonnysunm.com.acornote.databinding.ListFolderBinding
import tonnysunm.com.acornote.model.FolderWrapper

class FolderListAdapter :
    PagedListAdapter<FolderWrapper, FolderListAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ListFolderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<FolderWrapper>() {

            override fun areItemsTheSame(old: FolderWrapper, new: FolderWrapper) =
                old.folder.id == new.folder.id

            override fun areContentsTheSame(old: FolderWrapper, new: FolderWrapper) = old == new
        }
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