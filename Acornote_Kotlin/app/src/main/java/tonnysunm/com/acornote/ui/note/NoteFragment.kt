package tonnysunm.com.acornote.ui.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import tonnysunm.com.acornote.databinding.FragmentNoteBinding
import tonnysunm.com.acornote.model.EmptyId

class NoteFragment : Fragment() {
    val folderId: Long? by lazy {
        arguments?.getLong("folderId")
    }

    private val viewModel: NoteViewModel by viewModels {
        DetailViewModelFactory(
            requireActivity().application, folderId
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentNoteBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = this.viewModel

        val adapter = NoteListAdapter()
        viewModel.data.observe(this.viewLifecycleOwner, Observer { adapter.submitList(it) })
        binding.recyclerview.adapter = adapter

        binding.setOnAddNote {
            //            it.findNavController().navigate(
//                NoteFragmentDirections.actionDetailFragmentToEditNoteFragment(folderId, EmptyId)
//            )
        }

        return binding.root
    }

}