package tonnysunm.com.acornote.ui.note.edit

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tonnysunm.com.acornote.R
import tonnysunm.com.acornote.databinding.FragmentEditNoteBinding
import tonnysunm.com.acornote.model.EmptyId
import java.lang.IllegalStateException

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

        val intent = activity?.intent

        binding.setOnSure { view ->
            view.isEnabled = false

            val title = viewModel.noteEditing.title.value
                ?: throw IllegalStateException("title is null")

            val description = viewModel.noteEditing.description.value

            binding.progressbar.visibility = View.VISIBLE

            val labelId = intent?.extras?.getLong(getString(R.string.labelIdKey)) ?: 0
            val favourite = intent?.extras?.getBoolean("favourite") ?: false
            lifecycleScope.launch {
                viewModel.updateOrInsertNote(labelId, favourite, title, description)

                activity?.finish()
            }
        }

        viewModel.noteLiveData.observe(viewLifecycleOwner, Observer {
            viewModel.noteEditing.title.value = it.title
            viewModel.noteEditing.description.value = it.description

            intent?.let { intent ->
                if (intent.action == Intent.ACTION_SEND && "text/plain" == intent.type) {
                    intent.getStringExtra(Intent.EXTRA_TEXT)?.let { title ->
                        val regex = Regex("“.*”")
                        val match = regex.find(title)

                        val startChar: Char = "“".first()
                        val endChar: Char = "”".first()
                        viewModel.noteEditing.title.value =
                            match?.value?.trimStart(startChar)?.trimEnd(endChar)
                    }
                }
            }
        })

        binding.titleView.requestFocus()

        return binding.root
    }


}
