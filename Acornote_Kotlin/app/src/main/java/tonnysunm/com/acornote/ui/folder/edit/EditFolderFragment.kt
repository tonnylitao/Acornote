package tonnysunm.com.acornote.ui.folder

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import kotlinx.coroutines.launch
import tonnysunm.com.acornote.MainActivity
import tonnysunm.com.acornote.SharedViewModel
import tonnysunm.com.acornote.databinding.FragmentEditFolderBinding
import tonnysunm.com.acornote.extensions.hideSoftKeyboard
import tonnysunm.com.acornote.extensions.showSoftKeyboard
import tonnysunm.com.acornote.model.EmptyId
import tonnysunm.com.acornote.model.NoteFilter


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

        val binding = FragmentEditFolderBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val view = binding.root

        binding.setOnCancel {

            activity?.hideSoftKeyboard(binding.textView)

            it.findNavController().popBackStack()
        }

        binding.setOnSure { view ->
            view.isEnabled = false

            val title = binding.textView.text.toString()

            binding.progressbar.visibility = View.VISIBLE


            activity?.hideSoftKeyboard(binding.textView)

            lifecycleScope.launch {
                val id = viewModel.updateOrInsertFolder(title)
                
                activity?.let {
                    val mainModel = ViewModelProvider(it).get(SharedViewModel::class.java)
                    mainModel.noteFilterLiveData.value = NoteFilter.ByFolder(
                        id = id,
                        folderTitle = title
                    )
                }

                view.findNavController().popBackStack()
            }
        }

        activity?.showSoftKeyboard(binding.textView)

        return view
    }

    override fun onResume() {
        super.onResume()
//        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
//        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        (activity as AppCompatActivity).supportActionBar?.show()
    }

}
