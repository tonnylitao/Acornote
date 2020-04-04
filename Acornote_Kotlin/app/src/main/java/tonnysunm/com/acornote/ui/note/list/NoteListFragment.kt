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
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_notes.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tonnysunm.com.acornote.HomeSharedViewModel
import tonnysunm.com.acornote.R
import tonnysunm.com.acornote.databinding.FragmentNotesBinding
import tonnysunm.com.acornote.model.Note
import tonnysunm.com.acornote.model.NoteFilter
import kotlin.math.max
import kotlin.math.min


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
            Log.d("TAG count", it.count().toString())
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
                var noteIds = mutableSetOf<Long>()
                var notes = mutableSetOf<Note>()

                override fun isLongPressDragEnabled() =
                    mViewModel.noteFilterLiveData.value == NoteFilter.All

                override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
                    val fromNote = adapter.getItem(fromPosition) ?: return false
                    val toNote = adapter.getItem(toPosition) ?: return false

                    val order = toNote.order
                    toNote.order = fromNote.order
                    fromNote.order = order

                    if (!noteIds.contains(fromNote.id)) {
                        noteIds.add(fromNote.id)
                        notes.add(fromNote)
                    }

                    if (!noteIds.contains(toNote.id)) {
                        noteIds.add(toNote.id)
                        notes.add(toNote)
                    }

                    adapter.notifyItemMoved(fromPosition, toPosition)
                    return true
                }

                override fun onItemEndMove() {
                    mViewModel.viewModelScope.launch(Dispatchers.IO) {

                        if (notes.isNotEmpty()) {
                            mViewModel.updateNotes(notes)

                            //TODO bug: data saved into db success, but recylerView does not work well.
                            mViewModel.viewModelScope.launch(Dispatchers.Main) {
                                adapter.notifyDataSetChanged()
                            }

                            notes.clear()
                            noteIds.clear()
                        }

                    }
                }
            })
        )
        touchHelper.attachToRecyclerView(binding.recyclerview)

        return binding.root
    }

}