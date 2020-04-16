package tonnysunm.com.acornote.ui.colortag

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tonnysunm.com.acornote.databinding.ListItemColortagBinding
import tonnysunm.com.acornote.model.ColorTag
import java.util.*

class ColorTagListAdapter(var array: List<ColorTag>) :
    RecyclerView.Adapter<ColorTagListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ListItemColortagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount() = array.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(array[position])
    }

    /* ViewHolder */

    inner class ViewHolder(private val binding: ListItemColortagBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

        }

        fun bind(item: ColorTag) {
            binding.data = item

            binding.executePendingBindings()
        }
    }

}

class ColorTagHelper {
    companion object {
        @JvmStatic
        fun defaultColorName(item: ColorTag): String? {
            val name = item.color
            return mapOf(
                "#000000" to "Black",
                "#A4A4A4" to "Gray",
                "#56D769" to "Green",
                "#3D94FE" to "Blue",
                "#FFAA47" to "Orange",
                "#BF75E5" to "Purple",
                "#FF625C" to "Red",
                "#FFD64B" to "Yellow"
            )[name.toUpperCase(Locale.ROOT)]
        }
    }
}