package tonnysunm.com.acornote.ui.note.list

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import tonnysunm.com.acornote.MainActivity
import tonnysunm.com.acornote.R
import tonnysunm.com.acornote.SharedViewModel
import tonnysunm.com.acornote.databinding.FragmentNotesBinding
import tonnysunm.com.acornote.model.NoteFilter


class NoteListFragment : Fragment() {
    private lateinit var mainModel: SharedViewModel

    private val mViewModel: NoteListViewModel by viewModels {
        val filter = arguments?.getString("filter") ?: ""
        val labelId = arguments?.getLong(getString(R.string.labelIdKey)) ?: 0
        val labelTitle = arguments?.getString("labelTitle") ?: ""

        val noteFilter = when {
            filter == "star" -> NoteFilter.Star
            labelId > 0 -> NoteFilter.ByLabel(labelId, labelTitle)
            else -> NoteFilter.All
        }

        DetailViewModelFactory(requireActivity().application, noteFilter)
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
            Log.d("TAG", "$it")
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

        return binding.root
    }

    //
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val activity = activity as? MainActivity ?: throw Exception("Invalid Activity")
        val actionBar = activity.supportActionBar

        //
        mainModel = ViewModelProvider(activity).get(SharedViewModel::class.java)

        mainModel.noteFilterLiveData.observe(this.viewLifecycleOwner, Observer {
            mViewModel.noteFilterLiveData.value = it

            actionBar?.title = it.title
        })
    }


}