package tonnysunm.com.acornote.ui.note.list

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_notes.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tonnysunm.com.acornote.HomeSharedViewModel
import tonnysunm.com.acornote.R
import tonnysunm.com.acornote.databinding.FragmentNotesBinding
import tonnysunm.com.acornote.model.NoteFilter


private const val TAG = "NoteListFragment"

class NoteListFragment : Fragment() {

    private val labelId = arguments?.getLong(getString(R.string.labelIdKey))

    private val mViewModel: NoteListViewModel by viewModels {
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
            Log.d(TAG, "$it")
            adapter.submitList(it)
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
                var targetNoteId: Long? = null
                var targetNoteOrder: Long? = null

                override fun isLongPressDragEnabled() =
                    mViewModel.noteFilterLiveData.value == NoteFilter.All

                override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
                    if (targetNoteId == null) {
                        val note = adapter.getItem(fromPosition) ?: return false
                        targetNoteId = note.id
                        targetNoteOrder = note.order
                    }

                    adapter.notifyItemMoved(fromPosition, toPosition)
                    return true
                }

                override fun onItemEndMove(toPosition: Int) {
                    val noteId = targetNoteId ?: return
                    val noteOrder = targetNoteOrder ?: return
                    val toNoteOrder = adapter.getItem(toPosition)?.order ?: return
                    if (noteOrder == toNoteOrder) {
                        return
                    }

                    targetNoteId = null
                    mViewModel.viewModelScope.launch(Dispatchers.IO) {
                        mViewModel.moveItem(noteId, noteOrder, toNoteOrder)
                    }
                }
            })
        )
        touchHelper.attachToRecyclerView(binding.recyclerview)

        return binding.root
    }

}