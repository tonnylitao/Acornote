package tonnysunm.com.acornote.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import tonnysunm.com.acornote.databinding.DetailFragmentBinding

class DetailFragment : Fragment() {

    private val viewModel: DetailViewModel by viewModels {
        DetailViewModelFactory(
            requireActivity().application,
            arguments?.getInt("folderId") ?: throw IllegalArgumentException("#folderId is not defined." ))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val binding =  DetailFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = this.viewModel

        val adapter = ItemListAdapter()
        binding.recyclerview.adapter = adapter
        viewModel.data.observe(this.viewLifecycleOwner, Observer {
            adapter.setDataSource(it)
        })

        return binding.root
    }

}