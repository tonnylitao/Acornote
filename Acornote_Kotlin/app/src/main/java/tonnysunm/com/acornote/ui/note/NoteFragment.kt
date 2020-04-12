package tonnysunm.com.acornote.ui.note

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlinx.coroutines.launch
import tonnysunm.com.acornote.HomeActivity
import tonnysunm.com.acornote.HomeSharedViewModel
import tonnysunm.com.acornote.R
import tonnysunm.com.acornote.databinding.FragmentEditNoteBinding
import tonnysunm.com.acornote.model.EmptyId
import tonnysunm.com.acornote.model.Note
import java.lang.Exception

class NoteFragment : Fragment() {

    private val id by lazy {
        val id = activity?.intent?.getLongExtra("id", EmptyId)
        if (id != null && id > 0.toLong()) id else null
    }

    private var noteBeforeEditing: Note? = null

    private val viewModel: NoteViewModel by viewModels {
        EditNoteViewModelFactory(requireActivity().application, id)
    }

    private val homeSharedModel: HomeSharedViewModel by lazy {
        ViewModelProvider(requireActivity()).get(HomeSharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.data.observe(viewLifecycleOwner, Observer {
            if (it.id != 0.toLong()) {
                this.noteBeforeEditing = it.copy()
            }


//            intent?.let { intent ->
//                if (intent.action == Intent.ACTION_SEND && "text/plain" == intent.type) {
//                    intent.getStringExtra(Intent.EXTRA_TEXT)?.let { title ->
//                        val regex = Regex("“.*”")
//                        val match = regex.find(title)
//
//                        val startChar: Char = "“".first()
//                        val endChar: Char = "”".first()
//                        viewModel.noteEditing.title.value =
//                            match?.value?.trimStart(startChar)?.trimEnd(endChar)
//                    }
//                }
//            }
        })

        if (id == null) {
            binding.titleView.requestFocus()
        }

        binding.viewPager.adapter = ScreenSlidePagerAdapter(requireActivity())


        return binding.root
    }

    fun insertOrUpdateNote() {
        val labelId = activity?.intent?.extras?.get(getString(R.string.labelIdKey))
        val star = activity?.intent?.extras?.get(getString(R.string.starKey))

        val note = viewModel.data.value

        val validLabelId: Long? =
            if (labelId != null && labelId is Long && labelId > 0) labelId else null
        if (star != null) {
            note?.star = star == true
        }

        val noteBeforeEditing = this.noteBeforeEditing

        val isInsert = note?.id == null || note?.id == 0.toLong()
        val inUpdate = noteBeforeEditing != note

        if (!inUpdate && !inUpdate) {
            activity?.setResult(Activity.RESULT_CANCELED)
            activity?.finish()
            return
        }

        lifecycleScope.launch {
            try {
                if (isInsert) {
                    HomeActivity.scrollToTop = isInsert
                    viewModel.insertNote(validLabelId)
                } else if (inUpdate) {
                    viewModel.updateNote()
                }

                activity?.setResult(Activity.RESULT_OK)
                activity?.finish()
            } catch (e: Exception) {
            } finally {
                activity?.setResult(Activity.RESULT_CANCELED)
                activity?.finish()
            }
        }
    }

    inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment = ScreenSlidePageFragment().apply {
            if (position == 0) {
                context?.resources?.getColor(R.color.colorAccent, null)?.let {
                    view?.setBackgroundColor(it)
                }
            } else {
                context?.resources?.getColor(R.color.drawer_label_title, null)?.let {
                    view?.setBackgroundColor(it)
                }
            }

        }
    }
}

class ScreenSlidePageFragment : Fragment(R.layout.fragment_slide_page)