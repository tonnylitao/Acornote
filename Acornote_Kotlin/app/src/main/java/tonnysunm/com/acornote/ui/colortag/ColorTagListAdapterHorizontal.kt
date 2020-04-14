package tonnysunm.com.acornote.ui.colortag

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.findFragment
import androidx.paging.PagedListAdapter
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

    /* ViewHolder */

    inner class ViewHolder(private val binding: ListItemColortagHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.clickListener = View.OnClickListener {
                val data = binding.data ?: return@OnClickListener

                val fragment = it.findFragment<ColorTagListFragmentHorizontal>()
                fragment.navigateToNotesByColorTag(data.id)
            }
        }

        fun bind(item: ColorTag) {
            binding.data = item
            binding.executePendingBindings()
        }
    }

}