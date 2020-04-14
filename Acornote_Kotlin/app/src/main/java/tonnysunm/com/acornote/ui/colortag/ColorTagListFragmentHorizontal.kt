package tonnysunm.com.acornote.ui.colortag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import tonnysunm.com.acornote.HomeSharedViewModel
import tonnysunm.com.acornote.databinding.FragmentColortagsHorizontalBinding
import tonnysunm.com.acornote.model.ColorTag
import tonnysunm.com.acornote.model.NoteFilter

private val TAG = "ColorTagListFragmentHorizontal"

open class ColorTagListFragmentHorizontal : Fragment() {

    val viewModel: ColorTagViewModel by viewModels {
        ColorTagViewModelFactory(
            requireActivity().application
        )
    }

    private val homeSharedModel: HomeSharedViewModel by lazy {
        ViewModelProvider(requireActivity()).get(HomeSharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentColortagsHorizontalBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = ColorTagListAdapterHorizontal()
        viewModel.data.observe(this.viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        binding.drawerColortagRecyclerView.adapter = adapter

//        binding.editText.setOnEditorActionListener() { v, keyCode, _ ->
//            if (keyCode == EditorInfo.IME_ACTION_DONE) {
//                val textView = v as TextView
////                viewModel.createLabel(textView.text.toString())
//
//                textView.text = null
//                return@setOnEditorActionListener false
//            }
//            false
//        }

        return binding.root
    }

    fun navigateToNotesBy(colorTag: ColorTag) {
        homeSharedModel.noteFilterLiveData.value = NoteFilter.ByColorTag(colorTag)
    }
}
