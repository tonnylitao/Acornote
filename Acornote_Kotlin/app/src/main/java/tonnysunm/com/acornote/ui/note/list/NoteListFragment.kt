package tonnysunm.com.acornote.ui.note

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import tonnysunm.com.acornote.databinding.FragmentNoteBinding
import tonnysunm.com.acornote.model.NoteFilter

class NoteListFragment : Fragment() {
    private val filter: NoteFilter by lazy {
        val filter = arguments?.getString("filter") ?: ""
        val maybeId = filter.toLongOrNull() ?: 0

        when {
            filter == "favourite" -> NoteFilter.Favourite
            maybeId > 0 -> NoteFilter.ByFolder(maybeId)
            else -> NoteFilter.All
        }
    }

    private val listViewModel: NoteListViewModel by viewModels {
        DetailViewModelFactory(requireActivity().application, filter)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNoteBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = this.listViewModel

        val adapter = NoteListAdapter()
        listViewModel.data.observe(this.viewLifecycleOwner, Observer {
            Log.d("TAG", "$it")
            adapter.submitList(it)
        })
        binding.recyclerview.adapter = adapter

        binding.setOnAddNote {
            //            it.findNavController().navigate(
//                NoteFragmentDirections.actionDetailFragmentToEditNoteFragment(folderId, EmptyId)
//            )
        }

        return binding.root
    }

}