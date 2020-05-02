package tonnysunm.com.acornote.ui.note.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tonnysunm.com.acornote.R
import tonnysunm.com.acornote.databinding.FragmentNotesBinding
import tonnysunm.com.acornote.library.AndroidViewModelFactory
import tonnysunm.com.acornote.model.NoteFilter
import tonnysunm.com.acornote.ui.HomeActivity
import tonnysunm.com.acornote.ui.HomeSharedViewModel


private const val TAG = "NoteListFragment"

class NoteListFragment : Fragment() {

    private val labelId = arguments?.getInt(getString(R.string.labelIdKey))

    private val mViewModel by viewModels<NoteListViewModel> {
        val filter = arguments?.getString("filter") ?: ""
        val labelTitle = arguments?.getString("labelTitle") ?: ""

        val noteFilter = when {
            filter == getString(R.string.starKey) -> NoteFilter.Star
            (labelId ?: 0) > 0 -> NoteFilter.ByLabel(labelId!!, labelTitle)
            else -> NoteFilter.All
        }

        AndroidViewModelFactory(requireActivity().application, noteFilter)
    }

    private val homeSharedModel by activityViewModels<HomeSharedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val adapter = NoteListAdapter()

        val fragment = this
        val binding = FragmentNotesBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = fragment
            viewModel = fragment.mViewModel

            recyclerview.adapter = adapter
        }

        mViewModel.data.observe(this.viewLifecycleOwner, Observer {
            val adp = binding.recyclerview.adapter as NoteListAdapter
            adp.submitList(it)

            //delay for fix PagedStorageDiffHelper.computeDiff running in background thread
            if (NoteListAdapter.disableAnimation) {
                mViewModel.viewModelScope.launch {
                    delay(500)

                    adp.notifyDataSetChanged() //update viewholder's binding data
                    NoteListAdapter.disableAnimation = false
                }
            }
        })

        adapter.registerAdapterDataObserver(object :
            RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (!HomeActivity.scrollToTop) return

                HomeActivity.scrollToTop = false
                val manager = binding.recyclerview.layoutManager as? LinearLayoutManager
                manager?.smoothScrollToPosition(binding.recyclerview, null, 0)
            }
        })

        homeSharedModel.noteFilterLiveData.observe(this.viewLifecycleOwner, Observer {
            mViewModel.noteFilterLiveData.value = it
        })

        /*
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
        */

        return binding.root
    }

}
