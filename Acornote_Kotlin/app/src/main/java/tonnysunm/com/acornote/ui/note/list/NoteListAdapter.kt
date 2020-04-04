package tonnysunm.com.acornote.ui.note.list

import android.media.MediaRouter
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import tonnysunm.com.acornote.databinding.ListItemNoteBinding
import tonnysunm.com.acornote.model.Note


class NoteListAdapter :
    PagedListAdapter<Note, NoteListAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ListItemNoteBinding.inflate(
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
        private val DiffCallback = object : DiffUtil.ItemCallback<Note>() {

            override fun areItemsTheSame(old: Note, aNew: Note) = old.id == aNew.id

            override fun areContentsTheSame(old: Note, aNew: Note) = old == aNew
        }
    }


    /* ViewHolder */

    inner class ViewHolder(private val binding: ListItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.setClickListener {
                binding.data?.id?.let {

                }
            }
        }

        fun bind(note: Note) {
            binding.data = note
            binding.descriptionIsNullOrEmpty = note.description?.trim().isNullOrEmpty()

            binding.executePendingBindings()
        }
    }
}

interface ItemTouchHelperAdapter {
    fun isLongPressDragEnabled(): Boolean
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
//    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
//        if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
//            if (toPosition != null) {
//                mAdapter.onItemEndMove()
//            }
//            mAdapter.onItemEndMove()
//        }
//    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        mAdapter.onItemEndMove()
    }

    override fun isItemViewSwipeEnabled() = false
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
}