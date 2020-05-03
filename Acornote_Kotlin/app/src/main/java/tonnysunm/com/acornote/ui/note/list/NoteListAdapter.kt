package tonnysunm.com.acornote.ui.note.list

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.invoke
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.databinding.BindingAdapter
import androidx.fragment.app.findFragment
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import tonnysunm.com.acornote.GlideApp
import tonnysunm.com.acornote.databinding.ListItemNoteBinding
import tonnysunm.com.acornote.model.NoteWithImageUrl
import tonnysunm.com.acornote.ui.HomeActivity
import tonnysunm.com.acornote.ui.note.NoteActivity


class NoteListAdapter :
    PagedListAdapter<NoteWithImageUrl, NoteListAdapter.ViewHolder>(DiffCallback) {

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
                startForResult(Intent(activity, NoteActivity::class.java).apply {
                    putExtra("id", note.note.id)

                    Log.d("TAG", "put ${note.note.id}")
                })

            }
        }

        fun bind(note: NoteWithImageUrl) {
            binding.data = note

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