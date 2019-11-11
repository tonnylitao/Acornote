package tonnysunm.com.acornote.ui.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import kotlinx.coroutines.*
import tonnysunm.com.acornote.databinding.EditFolderFragmentBinding
import tonnysunm.com.acornote.model.EmptyId

class EditFolderFragment : Fragment() {

    private val viewModel: EditFolderViewModel by viewModels {
        val id = arguments?.getLong("folderId")

        EditFolderViewModelFactory(
            requireActivity().application,
            if (id != EmptyId) id else null
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = EditFolderFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.setOnCancel {
            it.findNavController().popBackStack()
        }

        binding.setOnSure { view ->
            view.isEnabled = false

            val title = binding.textView.text.toString()

            binding.progressbar.visibility = View.VISIBLE

            lifecycleScope.launch {
                viewModel.updateOrInsertFolder(title)

                view.findNavController().popBackStack()
            }
        }

        return binding.root
    }

}
