package tonnysunm.com.acornote.ui.note.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tonnysunm.com.acornote.databinding.ListNoteBinding
import tonnysunm.com.acornote.model.Note


class NoteListAdapter :
    PagedListAdapter<Note, NoteListAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ListNoteBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Note>() {

            override fun areItemsTheSame(old: Note, aNew: Note) = old.id == aNew.id

            override fun areContentsTheSame(old: Note, aNew: Note) = old == aNew
        }
    }

    /* ViewHolder */

    inner class ViewHolder(private val binding: ListNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.setClickListener {
                binding.data?.id?.let {

                }
            }
        }

        fun bind(note: Note) {
            binding.data = note
            binding.executePendingBindings()
        }
    }
}