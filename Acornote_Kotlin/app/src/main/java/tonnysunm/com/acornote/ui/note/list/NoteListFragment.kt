package tonnysunm.com.acornote.ui.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import tonnysunm.com.acornote.databinding.FragmentNoteBinding

class NoteListFragment : Fragment() {
    private val whereSQL: String by lazy {
        arguments?.getString("whereSQL") ?: ""
    }

    private val listViewModel: NoteListViewModel by viewModels {
        DetailViewModelFactory(requireActivity().application, whereSQL)
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
        listViewModel.data.observe(this.viewLifecycleOwner, Observer { adapter.submitList(it) })
        binding.recyclerview.adapter = adapter

        binding.setOnAddNote {
            //            it.findNavController().navigate(
//                NoteFragmentDirections.actionDetailFragmentToEditNoteFragment(folderId, EmptyId)
//            )
        }

        return binding.root
    }

}