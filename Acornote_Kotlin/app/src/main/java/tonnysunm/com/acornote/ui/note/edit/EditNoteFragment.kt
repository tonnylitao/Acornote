package tonnysunm.com.acornote.ui.note.edit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlinx.android.synthetic.main.fragment_edit_note.*
import kotlinx.coroutines.launch
import tonnysunm.com.acornote.R
import tonnysunm.com.acornote.databinding.FragmentEditNoteBinding
import tonnysunm.com.acornote.model.EmptyId
import java.lang.Error
import java.lang.Exception
import java.lang.IllegalStateException
import java.lang.Throwable

class EditNoteFragment : Fragment() {

    private val id by lazy {
        val id = activity?.intent?.getLongExtra("id", EmptyId)
        if (id != null && id > 0.toLong()) id else null
    }

    private val viewModel: EditNoteViewModel by viewModels {
        EditNoteViewModelFactory(requireActivity().application, id)
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
        val labelId = activity?.intent?.getLongExtra(getString(R.string.labelIdKey), 0)
        val star = activity?.intent?.getBooleanExtra(getString(R.string.starKey), false)

        lifecycleScope.launch {
            try {
                viewModel.updateOrInsertNote(
                    labelId = if (labelId != null && labelId > 0) labelId else null,
                    star = star
                )

                activity?.setResult(Activity.RESULT_OK)
                activity?.finish()
            } catch (e: Exception) {
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