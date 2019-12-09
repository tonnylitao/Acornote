package tonnysunm.com.acornote.ui.label

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import kotlinx.coroutines.launch
import tonnysunm.com.acornote.MainActivity
import tonnysunm.com.acornote.SharedViewModel
import tonnysunm.com.acornote.databinding.FragmentEditLabelBinding
import tonnysunm.com.acornote.extensions.hideSoftKeyboard
import tonnysunm.com.acornote.extensions.showSoftKeyboard
import tonnysunm.com.acornote.model.EmptyId
import tonnysunm.com.acornote.model.NoteFilter


class EditLabelFragment : Fragment() {

    private val viewModel: EditLabelViewModel by viewModels {
        val id = arguments?.getLong("labelId")

        EditLabelViewModelFactory(
            requireActivity().application,
            if (id != EmptyId) id else null
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentEditLabelBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val view = binding.root

        binding.setOnCancel {

            activity?.hideSoftKeyboard(binding.textView)

            it.findNavController().popBackStack()
        }

        binding.setOnSure {
            it.isEnabled = false

            val title = binding.textView.text.toString()

            binding.progressbar.visibility = View.VISIBLE


            activity?.hideSoftKeyboard(binding.textView)

            lifecycleScope.launch {
                val id = viewModel.updateOrInsertLabel(title)

                (activity as? MainActivity)?.let {
                    val mainModel = ViewModelProvider(it).get(SharedViewModel::class.java)
                    mainModel.noteFilterLiveData.value = NoteFilter.ByLabel(
                        id = id,
                        labelTitle = title
                    )
                }

                it.findNavController().popBackStack()
            }
        }

        viewModel.labelLiveData.observe(viewLifecycleOwner, Observer {
            viewModel.labelEditing.title.value = it.title
            viewModel.labelEditing.flippable.value = it.flippable
        })

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
