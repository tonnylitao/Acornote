package tonnysunm.com.acornote.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import tonnysunm.com.acornote.databinding.EditFolderFragmentBinding
import tonnysunm.com.acornote.model.Folder

class EditFolderFragment : Fragment() {

    private val viewModel: EditFolderViewModel by viewModels {
        EditFolderViewModelFactory(
            requireActivity().application,
            arguments?.getInt("folder"))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val binding =  EditFolderFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = this.viewModel

        binding.setOnCancel {

        }

        binding.setOnSure {

        }

        return binding.root
    }

}