package tonnysunm.com.acornote.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tonnysunm.com.acornote.databinding.EditItemFragmentBinding
import tonnysunm.com.acornote.model.EmptyId

class EditItemFragment : Fragment() {

    private val viewModel: EditItemViewModel by viewModels {
        val folderId = arguments?.getLong("folderId")
            ?: throw IllegalArgumentException("folderId is null.")

        val id = arguments?.getLong("id")?.let {
            if (it != EmptyId) it else null
        }

        EditItemViewModelFactory(requireActivity().application, id, folderId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = EditItemFragmentBinding.inflate(inflater, container, false)
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

            viewModel.viewModelScope.launch {
                viewModel.updateOrInsertItem(title, description)

                view.findNavController().popBackStack()
            }
        }

        return binding.root
    }

}
