package tonnysunm.com.acornote.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tonnysunm.com.acornote.databinding.EditFolderFragmentBinding
import tonnysunm.com.acornote.model.EmptyId

class EditFolderFragment : Fragment() {

    private val viewModel: EditFolderViewModel by viewModels {
        val id = arguments?.getLong("folderId")

        EditFolderViewModelFactory(
            requireActivity().application,
            if (id != EmptyId) id else null )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val binding =  EditFolderFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.setOnCancel {
            it.findNavController().popBackStack()
        }

        binding.setOnSure {view ->
            view.isEnabled = false

            val title = binding.textView.text.toString()

            binding.progressbar.visibility = View.VISIBLE

            viewModel.viewModelScope.launch {
                viewModel.updateOrInsertFolderAsync(title).await()

                withContext(Dispatchers.Main) {
                    view.findNavController().popBackStack()
                }
            }
        }

        return binding.root
    }

}
