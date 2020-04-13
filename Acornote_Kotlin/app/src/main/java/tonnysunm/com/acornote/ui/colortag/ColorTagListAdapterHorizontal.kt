package tonnysunm.com.acornote.ui.colortag

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tonnysunm.com.acornote.databinding.ListItemColortagHorizontalBinding
import tonnysunm.com.acornote.model.ColorTag

class ColorTagListAdapterHorizontal :
    PagedListAdapter<ColorTag, ColorTagListAdapterHorizontal.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ListItemColortagHorizontalBinding.inflate(
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

    inner class ViewHolder(private val binding: ListItemColortagHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
        }

        fun bind(item: ColorTag) {
            binding.data = item
            binding.executePendingBindings()
        }
    }

}