package tonnysunm.com.acornote.ui.main

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tonnysunm.com.acornote.R
import tonnysunm.com.acornote.model.Folder
import kotlin.math.log

class FolderListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<FolderListAdapter.FolderViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var items = emptyList<Folder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_folder, parent, false)

        return FolderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val current = items[position]
        holder.titleTxtView.text = current.title
    }

    internal fun setFolders(words: List<Folder>) {
        this.items = words
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    fun itemOf(index: Int): Folder = items[index]


    //
    inner class FolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTxtView: TextView = itemView.findViewById(R.id.title)

    }
}