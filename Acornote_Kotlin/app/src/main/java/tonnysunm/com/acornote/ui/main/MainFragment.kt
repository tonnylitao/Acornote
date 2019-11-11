package tonnysunm.com.acornote.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import tonnysunm.com.acornote.databinding.MainFragmentBinding
import tonnysunm.com.acornote.model.EmptyId

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = MainFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = this.viewModel

        val adapter = FolderListAdapter()
        viewModel.data.observe(this.viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
        binding.recyclerview.adapter = adapter

        binding.setOnAddFolder {
            it.findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToEditFolderFragment(EmptyId)
            )
        }

        return binding.root
    }

}
