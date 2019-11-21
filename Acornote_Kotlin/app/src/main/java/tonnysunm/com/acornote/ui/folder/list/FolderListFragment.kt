package tonnysunm.com.acornote.ui.folder

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import kotlinx.coroutines.launch
import tonnysunm.com.acornote.databinding.FragmentMainBinding
import tonnysunm.com.acornote.model.EmptyId
import kotlin.coroutines.coroutineContext

class FolderListFragment : Fragment() {

    private val viewModel: FolderListViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("TAG", "onCreateView")

        val binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = this.viewModel

        val adapter = FolderListAdapter()
        viewModel.data.observe(this.viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
        binding.recyclerview.adapter = adapter

        binding.setOnAddFolder {
            it.findNavController().navigate(
                FolderListFragmentDirections.actionMainFragmentToEditFolderFragment(EmptyId)
            )
        }

        return binding.root
    }

}
