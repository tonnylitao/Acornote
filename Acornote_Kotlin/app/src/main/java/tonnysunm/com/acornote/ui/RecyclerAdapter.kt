package tonnysunm.com.acornote.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter<M : IDEquable>(
    diffCallback: DiffUtil.ItemCallback<RecyclerItem<M>>,
    private val clickListener: ((M) -> Unit)? = null
) : PagedListAdapter<RecyclerItem<M>, RecyclerAdapter<M>.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), viewType, parent, false)
        ).apply {
            itemView.setOnClickListener {
                val item = getItem(this.absoluteAdapterPosition) ?: return@setOnClickListener
                clickListener?.invoke(item.data)
            }
        }

    override fun getItemViewType(position: Int) =
        getItem(position)?.layoutId ?: throw IllegalArgumentException("position $position")

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.bind(holder.binding)
    }

    /* ViewHolder */
    inner class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)
}

interface IDEquable {
    val id: Int
}

data class RecyclerItem<M : IDEquable>(
    val data: M,
    @LayoutRes val layoutId: Int,
    val variableId: Int? //BR
) {

    fun bind(binding: ViewDataBinding) {
        variableId ?: return

        binding.setVariable(variableId, data)
        binding.executePendingBindings()
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<RecyclerItem<IDEquable>>() {
            override fun areItemsTheSame(
                old: RecyclerItem<IDEquable>,
                aNew: RecyclerItem<IDEquable>
            ) = old === aNew || old.data.id == aNew.data.id

            override fun areContentsTheSame(
                old: RecyclerItem<IDEquable>,
                aNew: RecyclerItem<IDEquable>
            ) = old == aNew
        }
    }
}

@Suppress("UNCHECKED_CAST")
fun <M : IDEquable> Class<M>.diffCallback(): DiffUtil.ItemCallback<RecyclerItem<M>> {
    return RecyclerItem.DiffCallback as DiffUtil.ItemCallback<RecyclerItem<M>>
}
