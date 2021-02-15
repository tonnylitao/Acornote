package tonnysunm.com.acornote.ui.note.list

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isGone
import androidx.databinding.BindingAdapter
import androidx.fragment.app.findFragment
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import tonnysunm.com.acornote.GlideApp
import tonnysunm.com.acornote.databinding.ListItemNoteBinding
import tonnysunm.com.acornote.model.NoteWithImageUrl
import tonnysunm.com.acornote.ui.note.NoteActivity


class NoteListAdapter :
    PagedListAdapter<NoteWithImageUrl, NoteListAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ListItemNoteBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        ).apply {
            itemView.setOnClickListener {
                this@NoteListAdapter.clickViewHolder(this)
            }
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) ?: return

        holder.binding.data = item
        holder.binding.executePendingBindings()
    }

    private fun clickViewHolder(viewHolder: ViewHolder) {
        val item = getItem(viewHolder.absoluteAdapterPosition) ?: return
        val activity =
            viewHolder.itemView.findFragment<NoteListFragment>().activity
                ?: return

        val intent = Intent(activity, NoteActivity::class.java).apply {
            putExtra("id", item.note.id)

            Timber.d("put ${item.note.id}")
        }
        activity.startActivity(intent)
    }

    companion object {
        //heck for drag and drop to move items in PagedList
        var disableAnimation = false

        private val DiffCallback = object : DiffUtil.ItemCallback<NoteWithImageUrl>() {

            override fun areItemsTheSame(old: NoteWithImageUrl, aNew: NoteWithImageUrl): Boolean {
                return disableAnimation || old.note.id == aNew.note.id
            }

            override fun areContentsTheSame(
                old: NoteWithImageUrl,
                aNew: NoteWithImageUrl
            ): Boolean {
                return disableAnimation || old == aNew
            }
        }
    }


    /* ViewHolder */

    inner class ViewHolder(val binding: ListItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root)
}

/* ItemTouchHelperAdapter */

interface ItemTouchHelperAdapter {
    fun isLongPressDragEnabled(): Boolean

    fun onItemStartMove()
    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
    fun onItemEndMove()
}

class ItemTouchHelperCallback(adapter: ItemTouchHelperAdapter) :
    ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {

    private val mAdapter = adapter

    private var toPosition: Int? = null

    override fun isLongPressDragEnabled() = mAdapter.isLongPressDragEnabled()

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        toPosition = target.adapterPosition

        return mAdapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            mAdapter.onItemStartMove()
        } else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
            mAdapter.onItemEndMove()
        }
    }

    override fun isItemViewSwipeEnabled() = false
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
}


@BindingAdapter("android:layout_marginEnd")
fun setLayoutMarginBottom(view: View, dimen: Float) {
    val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.marginEnd = dimen.toInt()
    view.layoutParams = layoutParams
}

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
    if (view.isGone || imageUrl == null) return

    GlideApp.with(view.context)
        .load(imageUrl)
        .into(view)
}