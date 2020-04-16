package tonnysunm.com.acornote.ui.colortag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import tonnysunm.com.acornote.databinding.FragmentColortagsBinding

private val TAG = "ColorTagListFragment"

open class ColorTagListFragment(private val vertical: Boolean = true) : Fragment() {

    val viewModel: ColorTagViewModel by viewModels {
        ColorTagViewModelFactory(
            requireActivity().application
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentColortagsBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = ColorTagListAdapter(listOf())
        viewModel.data.observe(this.viewLifecycleOwner, Observer {
            adapter.array = it
            adapter.notifyDataSetChanged()
        })

        binding.recyclerview.adapter = adapter

        return binding.root
    }

}
