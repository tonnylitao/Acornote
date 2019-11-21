package tonnysunm.com.acornote.ui.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import kotlinx.coroutines.launch
import tonnysunm.com.acornote.databinding.EditNoteFragmentBinding
import tonnysunm.com.acornote.model.EmptyId

class EditNoteFragment : Fragment() {

    private val viewModel: EditNoteViewModel by viewModels {
        val folderId = arguments?.getLong("folderId")
            ?: throw IllegalArgumentException("folderId is null.")

        val id = arguments?.getLong("id")?.let {
            if (it != EmptyId) it else null
        }

        EditNoteViewModelFactory(requireActivity().application, id, folderId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = EditNoteFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.setOnCancel {
            it.findNavController().popBackStack()
        }

        binding.setOnSure { view ->
            view.isEnabled = false

            val title = binding.titleView.text.toString()
            val description = (binding.descriptionView.text ?: "").toString()

            binding.progressbar.visibility = View.VISIBLE

            lifecycleScope.launch {
                viewModel.updateOrInsertNote(title, description)

                view.findNavController().popBackStack()
            }
        }

        return binding.root
    }

}
