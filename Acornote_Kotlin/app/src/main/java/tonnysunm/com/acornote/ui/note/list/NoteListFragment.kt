package tonnysunm.com.acornote.ui.note.list

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_notes.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import tonnysunm.com.acornote.HomeSharedViewModel
import tonnysunm.com.acornote.R
import tonnysunm.com.acornote.databinding.FragmentNotesBinding
import tonnysunm.com.acornote.model.Note
import tonnysunm.com.acornote.model.NoteFilter
import java.util.*


private const val TAG = "NoteListFragment"

class NoteListFragment : Fragment() {

    private val labelId = arguments?.getLong(getString(R.string.labelIdKey))

    val mViewModel: NoteListViewModel by viewModels {
        val filter = arguments?.getString("filter") ?: ""
        val labelTitle = arguments?.getString("labelTitle") ?: ""

        val noteFilter = when {
            filter == getString(R.string.starKey) -> NoteFilter.Star
            (labelId ?: 0) > 0 -> NoteFilter.ByLabel(labelId!!, labelTitle)
            else -> NoteFilter.All
        }

        DetailViewModelFactory(requireActivity().application, noteFilter)
    }

    private val homeSharedModel: HomeSharedViewModel by lazy {
        ViewModelProvider(requireActivity()).get(HomeSharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNotesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = this.mViewModel

        val adapter = NoteListAdapter()
        mViewModel.data.observe(this.viewLifecycleOwner, Observer {
            adapter.submitList(it)

            //delay for fix PagedStorageDiffHelper.computeDiff running in background thread
            if (NoteListAdapter.disableAnimation) {
                mViewModel.viewModelScope.launch {
                    delay(500)

                    adapter.notifyDataSetChanged() //update viewholder's binding data
                    NoteListAdapter.disableAnimation = false
                }
            }
        })
        binding.recyclerview.adapter = adapter

        binding.recyclerview.addItemDecoration(
            object : DividerItemDecoration(
                activity,
                VERTICAL
            ) {
                override fun getItemOffsets(
                    outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
                ) {
                    val padding = parent.paddingLeft
                    val bottom = drawable?.intrinsicHeight ?: 0

                    outRect.set(Rect(0, padding, padding, padding + bottom))
                }
            }
        )

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                val manager = recyclerview.layoutManager
                manager?.smoothScrollToPosition(recyclerview, null, positionStart)
            }
        })

        homeSharedModel.noteFilterLiveData.observe(this.viewLifecycleOwner, Observer {
            mViewModel.noteFilterLiveData.value = it

            val actionBar = (activity as? AppCompatActivity)?.supportActionBar
            actionBar?.title = it.title
        })

        val touchHelper = ItemTouchHelper(
            ItemTouchHelperCallback(object : ItemTouchHelperAdapter {
                //get correct note from fromPosition and toPosition after swap
                private var tempList: MutableList<Note>? = null

                private var toUpdateNotes: MutableList<Note>? = null

                override fun isLongPressDragEnabled() =
                    mViewModel.noteFilterLiveData.value == NoteFilter.All

                override fun onItemStartMove() {
                    //get most the most updated data
                    tempList = adapter.currentList?.toMutableList()

                    toUpdateNotes = mutableListOf()
                }

                override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
                    val fromNote = tempList?.get(fromPosition) ?: return false
                    val toNote = tempList?.get(toPosition) ?: return false

                    val order = toNote.order
                    toNote.order = fromNote.order
                    fromNote.order = order

                    toUpdateNotes?.removeAll { it.id == fromNote.id || it.id == toNote.id }
                    toUpdateNotes?.add(fromNote)
                    toUpdateNotes?.add(toNote)

                    Collections.swap(tempList!!, fromPosition, toPosition)
                    adapter.notifyItemMoved(fromPosition, toPosition)

                    return true
                }

                override fun onItemEndMove() {
                    Log.d("TAG", "onItemEndMove")
                    tempList = null

                    if (!toUpdateNotes.isNullOrEmpty()) {
                        mViewModel.viewModelScope.launch(Dispatchers.IO) {
                            /*
                            save into db will cause mViewMode.data being posted new value
                            because of pagedList data did not change, so it will have animation after find the diff
                            NoteListAdapter.disableAnimation will fix this animation*
                            */
                            NoteListAdapter.disableAnimation = true

                            val count = mViewModel.updateNotes(toUpdateNotes!!)
                            Log.d("TAG", "updated $count")

                            toUpdateNotes = null
                        }
                    }
                }
            })
        )

        touchHelper.attachToRecyclerView(binding.recyclerview)

        return binding.root
    }

}
