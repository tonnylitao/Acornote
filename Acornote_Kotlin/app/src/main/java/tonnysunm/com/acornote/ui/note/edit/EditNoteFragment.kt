package tonnysunm.com.acornote.ui.note

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import kotlinx.coroutines.launch
import tonnysunm.com.acornote.R
import tonnysunm.com.acornote.databinding.FragmentEditNoteBinding
import tonnysunm.com.acornote.model.EmptyId

class EditNoteFragment : Fragment() {

    private val viewModel: EditNoteViewModel by viewModels {
        val id = activity?.intent?.extras?.getLong("id")?.let {
            if (it != EmptyId) it else null
        }

        EditNoteViewModelFactory(requireActivity().application, id)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.setOnCancel {
            activity?.finish()
        }

        binding.setOnSure { view ->
            view.isEnabled = false

            val title = binding.titleView.text.toString()
            val description = (binding.descriptionView.text ?: "").toString()

            binding.progressbar.visibility = View.VISIBLE

            val folderId = activity?.intent?.extras?.getLong(getString(R.string.folderIdKey))
            val favourite = activity?.intent?.extras?.getBoolean("favourite") ?: false
            lifecycleScope.launch {
                viewModel.updateOrInsertNote(folderId, favourite, title, description)

                activity?.finish()
            }
        }

        return binding.root
    }

}
