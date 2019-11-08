package tonnysunm.com.acornote.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import tonnysunm.com.acornote.databinding.MainFragmentBinding
import tonnysunm.com.acornote.model.EmptyId
import tonnysunm.com.acornote.model.Folder

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
        binding.recyclerview.adapter = adapter
        viewModel.data.observe(this.viewLifecycleOwner, Observer {
            adapter.setDataSource(it)
        })

        binding.setOnAddFolder {
            it.findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToEditFolderFragment(EmptyId)
            )
        }

        return binding.root
    }

}
