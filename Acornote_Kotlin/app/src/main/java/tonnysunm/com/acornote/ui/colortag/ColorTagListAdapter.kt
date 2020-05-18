package tonnysunm.com.acornote.ui.colortag

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tonnysunm.com.acornote.databinding.ListItemColortagBinding
import tonnysunm.com.acornote.model.ColorTag

class ColorTagListAdapter(var array: List<ColorTag>) :
    RecyclerView.Adapter<ColorTagListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ListItemColortagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount() = array.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = array[position]

        holder.binding.data = item
        holder.binding.executePendingBindings()
    }

    /* ViewHolder */

    inner class ViewHolder(val binding: ListItemColortagBinding) :
        RecyclerView.ViewHolder(binding.root)

}