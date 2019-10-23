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
import tonnysunm.com.acornote.model.FolderWrapper
import tonnysunm.com.acornote.model.Repository
import kotlin.math.log

class FolderListAdapter(context: Context, private val repository: Repository): RecyclerView.Adapter<FolderListAdapter.FolderViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var items = emptyList<FolderWrapper>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_folder, parent, false)

        return FolderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val folderWrapper = items[position]
        holder.titleTxtView.text = folderWrapper.folder.title

        holder.countTxtView.text = folderWrapper.itemCount.toString() + " items"
    }

    internal fun setFolders(words: List<FolderWrapper>) {
        this.items = words
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    fun itemOf(index: Int): FolderWrapper = items[index]


    //
    inner class FolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTxtView: TextView = itemView.findViewById(R.id.title)
        val countTxtView: TextView = itemView.findViewById(R.id.count)

    }
}