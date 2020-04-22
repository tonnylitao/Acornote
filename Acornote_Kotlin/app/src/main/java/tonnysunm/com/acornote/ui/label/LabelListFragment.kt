package tonnysunm.com.acornote.ui.label

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import tonnysunm.com.acornote.databinding.FragmentLabelsBinding
import tonnysunm.com.acornote.model.EmptyId

private val TAG = "LabelListFragment"

class LabelListFragment : Fragment() {
    private val id by lazy {
        val id = activity?.intent?.getLongExtra("id", EmptyId)
        Log.d(TAG, "$id")
        if (id != null && id > 0L) id else 0L
    }

    val viewModel: EditLabelViewModel by viewModels {
        EditLabelViewModelFactory(
            requireActivity().application,
            id
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val adapter = LabelListAdapter()

        val fragment = this
        val binding = FragmentLabelsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = fragment
            viewModel = fragment.viewModel

            recyclerview.adapter = adapter

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
            Log.d(TAG, it.toString())
            adapter.submitList(it)
        })

        return binding.root
    }

}
