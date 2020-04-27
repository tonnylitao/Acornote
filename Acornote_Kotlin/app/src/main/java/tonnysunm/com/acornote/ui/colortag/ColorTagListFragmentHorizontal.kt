package tonnysunm.com.acornote.ui.colortag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import tonnysunm.com.acornote.ui.HomeActivity
import tonnysunm.com.acornote.ui.HomeSharedViewModel
import tonnysunm.com.acornote.R
import tonnysunm.com.acornote.databinding.FragmentColortagsHorizontalBinding
import tonnysunm.com.acornote.model.ColorTag
import tonnysunm.com.acornote.model.NoteFilter
import tonnysunm.com.acornote.ui.note.NoteFragment

private val TAG = "ColorTagListFragmentHorizontal"

open class ColorTagListFragmentHorizontal : Fragment() {

    val viewModel: ColorTagViewModel by viewModels {
        ColorTagViewModelFactory(
            requireActivity().application
        )
    }

    private val homeSharedModel: HomeSharedViewModel? by lazy {
        val activity = requireActivity()
        if (activity is HomeActivity) {
            ViewModelProvider(requireActivity()).get(HomeSharedViewModel::class.java)
        } else {
            null
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentColortagsHorizontalBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = ColorTagListAdapterHorizontal(null, listOf())

        val editNoteFragment =
            activity?.supportFragmentManager?.findFragmentById(R.id.fragment_edit_note) as? NoteFragment
        editNoteFragment?.viewModel?.data?.observe(this.viewLifecycleOwner, Observer {
            adapter.selectedColorTagId = it?.colorTagId //? fix note been deleted
            adapter.notifyDataSetChanged()
        })

        homeSharedModel?.noteFilterLiveData?.observe(this.viewLifecycleOwner, Observer {
            adapter.selectedColorTagId = it.colorTagId
            adapter.notifyDataSetChanged()
        })

        viewModel.data.observe(this.viewLifecycleOwner, Observer {
            adapter.array = it
            adapter.notifyDataSetChanged()
        })

        binding.drawerColortagRecyclerView.adapter = adapter

        return binding.root
    }

    fun navigateToNotesBy(colorTag: ColorTag) {
        homeSharedModel?.noteFilterLiveData?.value = NoteFilter.ByColorTag(colorTag)
    }
}
