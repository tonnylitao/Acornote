package tonnysunm.com.acornote.ui.note

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.invoke
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tonnysunm.com.acornote.HomeActivity
import tonnysunm.com.acornote.R
import tonnysunm.com.acornote.databinding.FragmentNoteBinding
import tonnysunm.com.acornote.model.EmptyId
import tonnysunm.com.acornote.model.Note
import tonnysunm.com.acornote.ui.label.LabelListActivity

class NoteFragment : Fragment() {

    var menu: Menu? = null

    private val id by lazy {
        val id = activity?.intent?.getLongExtra("id", EmptyId)
        if (id != null && id > 0L) id else null
    }

    private var noteBeforeEditing: Note? = null

    val viewModel: NoteViewModel by viewModels {
        EditNoteViewModelFactory(requireActivity().application, id)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNoteBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.data.observe(viewLifecycleOwner, Observer {
            if (it != null) { //been deleted
                if (it.id != 0L) {
                    this.noteBeforeEditing = it.copy()
                }

                updateMenuItems(it)
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

        binding.editLabel = View.OnClickListener {
            val startForResult =
                requireActivity().prepareCall(ActivityResultContracts.StartActivityForResult()) {
                    if (it.resultCode == AppCompatActivity.RESULT_OK) {
                    }
                }
            startForResult(Intent(context, LabelListActivity::class.java).apply {
                putExtra("id", id)
            })
        }

        binding.editColor = View.OnClickListener {

        }

        setHasOptionsMenu(true)

        return binding.root
    }

    private fun updateMenuItems(it: Note) {
        val star = this.menu?.findItem(R.id.action_star)
        star?.setIcon(if (it.star == true) R.drawable.ic_stared else R.drawable.ic_star)

        val pin = this.menu?.findItem(R.id.action_pin)
        pin?.setIcon(if (it.pinned == true) R.drawable.ic_pinned else R.drawable.ic_pin)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.note, menu)

        super.onCreateOptionsMenu(menu, inflater)
        this.menu = menu

        viewModel.data.value?.let {
            updateMenuItems(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_star -> {
            val note = viewModel.data.value
            note?.star = viewModel.data.value?.star != true
            if (note?.id == 0L) {
                updateMenuItems(note)
            } else {
                viewModel.viewModelScope.launch(Dispatchers.IO) {
                    viewModel.updateNote()
                }
            }
            true
        }
        R.id.action_pin -> {
            val note = viewModel.data.value
            note?.pinned = viewModel.data.value?.pinned != true
            if (note?.id == 0L) {
                updateMenuItems(note)
            } else {
                viewModel.viewModelScope.launch(Dispatchers.IO) {
                    viewModel.updateNote()
                }
            }
            true
        }
        R.id.action_delete -> {
            viewModel.deleteNote()

            activity?.setResult(Activity.RESULT_CANCELED)
            activity?.finish()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
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

        val isInsert = note?.id == null || note.id == 0L
        val inUpdate = noteBeforeEditing != note

        if (!inUpdate && !inUpdate) {
            activity?.setResult(Activity.RESULT_CANCELED)
            activity?.finish()
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
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

    inner class ScreenSlidePagerAdapter(fa: FragmentActivity) :
        FragmentStateAdapter(fa) {
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