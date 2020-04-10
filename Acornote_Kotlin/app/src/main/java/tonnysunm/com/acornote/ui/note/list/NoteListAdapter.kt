package tonnysunm.com.acornote.ui.note.list

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.invoke
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.findFragment
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_label.view.*
import tonnysunm.com.acornote.HomeActivity
import tonnysunm.com.acornote.R
import tonnysunm.com.acornote.databinding.ListItemNoteBinding
import tonnysunm.com.acornote.model.Note
import tonnysunm.com.acornote.model.NoteFilter
import tonnysunm.com.acornote.ui.note.edit.EditNoteActivity
import tonnysunm.com.acornote.ui.note.edit.EditNoteFragment


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
        //heck for drag and drop to move items in PagedList
        var disableAnimation = false

        private val DiffCallback = object : DiffUtil.ItemCallback<Note>() {

            override fun areItemsTheSame(old: Note, aNew: Note): Boolean {
                return disableAnimation || old.id == aNew.id
            }

            override fun areContentsTheSame(old: Note, aNew: Note): Boolean {
                return disableAnimation || old == aNew
            }
        }
    }


    /* ViewHolder */

    inner class ViewHolder(private val binding: ListItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.clickListener = View.OnClickListener {
                val note = binding.data ?: return@OnClickListener

                val activity = it.findFragment<NoteListFragment>().activity as? HomeActivity
                    ?: return@OnClickListener

                val startForResult =
                    activity.prepareCall(ActivityResultContracts.StartActivityForResult()) {
                        if (it.resultCode == AppCompatActivity.RESULT_OK) {
                        }
                    }
                startForResult(Intent(activity, EditNoteActivity::class.java).apply {
                    putExtra("id", note.id)

                    Log.d("TAG", "put ${note.id}")
                })

            }
        }

        fun bind(note: Note) {
            binding.data = note
            binding.descriptionIsNullOrEmpty = note.description?.trim().isNullOrEmpty()

            binding.executePendingBindings()
        }
    }

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