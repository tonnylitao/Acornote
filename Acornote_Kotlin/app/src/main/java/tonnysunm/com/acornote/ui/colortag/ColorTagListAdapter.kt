package tonnysunm.com.acornote.ui.colortag

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tonnysunm.com.acornote.databinding.ListItemColortagBinding
import tonnysunm.com.acornote.model.ColorTag

class ColorTagListAdapter :
    PagedListAdapter<ColorTag, ColorTagListAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ListItemColortagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    public override fun getItem(position: Int) = super.getItem(position)

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ColorTag>() {
            override fun areItemsTheSame(
                old: ColorTag,
                aNew: ColorTag
            ): Boolean {
                return old.id == aNew.id
            }

            override fun areContentsTheSame(
                old: ColorTag,
                aNew: ColorTag
            ): Boolean {
                return old == aNew
            }
        }
    }


    /* ViewHolder */

    inner class ViewHolder(private val binding: ListItemColortagBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.clickListener = View.OnClickListener {
                val data = binding.data ?: return@OnClickListener

//                val fragment = it.findFragment<ColorTagListFragment>()
//                fragment.viewModel.flipChecked(data)
            }
        }

        fun bind(item: ColorTag) {
            binding.data = item

            binding.executePendingBindings()
        }
    }

}