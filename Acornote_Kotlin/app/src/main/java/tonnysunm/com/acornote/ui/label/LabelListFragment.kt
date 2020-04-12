package tonnysunm.com.acornote.ui.label

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import tonnysunm.com.acornote.databinding.FragmentLabelsBinding


class LabelListFragment : Fragment() {

    private val viewModel: EditLabelViewModel by viewModels {
        EditLabelViewModelFactory(
            requireActivity().application
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentLabelsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = LabelListAdapter()
        viewModel.data.observe(this.viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        return binding.root
    }

}
