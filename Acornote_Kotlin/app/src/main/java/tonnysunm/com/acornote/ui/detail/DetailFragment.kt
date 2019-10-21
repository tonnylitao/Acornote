package tonnysunm.com.acornote.ui.detail

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels

import tonnysunm.com.acornote.databinding.DetailFragmentBinding

class DetailFragment : Fragment() {

    companion object {
        fun newInstance() = DetailFragment()
    }

    private val viewModel: DetailViewModel by viewModels {
        DetailViewModelFactory(
            requireActivity().application,
            arguments?.getString("folderTitle") ?: throw IllegalArgumentException("#folderTitle is not defined." ))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding =  DetailFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            this.lifecycleOwner = this@DetailFragment
            this.viewModel = this@DetailFragment.viewModel
        }

        return binding.root
    }

}
