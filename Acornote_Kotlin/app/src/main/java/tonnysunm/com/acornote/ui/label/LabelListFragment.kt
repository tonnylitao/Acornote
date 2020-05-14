package tonnysunm.com.acornote.ui.label

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import timber.log.Timber
import tonnysunm.com.acornote.databinding.FragmentLabelsBinding
import tonnysunm.com.acornote.library.AndroidViewModelFactory
import tonnysunm.com.acornote.model.EmptyId

private val TAG = "LabelListFragment"

class LabelListFragment : Fragment() {
    private val noteId by lazy {
        val id = activity?.intent?.getIntExtra("id", EmptyId)
        Timber.d("$id")
        if (id != null && id > 0) id else 0
    }

    val viewModel by viewModels<EditLabelViewModel> {
        AndroidViewModelFactory(requireActivity().application, noteId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val fragment = this
        val binding = FragmentLabelsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = fragment
            viewModel = fragment.viewModel

            recyclerview.adapter = LabelListAdapter()

            editText.setOnEditorActionListener() { v, keyCode, _ ->
                if (keyCode == EditorInfo.IME_ACTION_DONE) {
                    val textView = v as TextView
                    fragment.viewModel.createLabel(textView.text.toString())

                    textView.text = null
                    return@setOnEditorActionListener false
                }
                false
            }
        }

        viewModel.data.observe(this.viewLifecycleOwner, Observer {
            val adapter = binding.recyclerview.adapter as LabelListAdapter

            adapter.submitList(it)
        })

        return binding.root
    }

}
