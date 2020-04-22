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

open class ColorTagListFragment() : Fragment() {

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
        val fragment = this
        val binding = FragmentColortagsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = fragment
            viewModel = fragment.viewModel

            recyclerview.adapter = ColorTagListAdapter(listOf())
        }

        viewModel.data.observe(this.viewLifecycleOwner, Observer {
            val adapter = binding.recyclerview.adapter as ColorTagListAdapter
            adapter.array = it
            adapter.notifyDataSetChanged()
        })

        return binding.root
    }

}
