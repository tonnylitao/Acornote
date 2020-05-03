package tonnysunm.com.acornote.ui.note

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
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
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tonnysunm.com.acornote.R
import tonnysunm.com.acornote.databinding.FragmentNoteBinding
import tonnysunm.com.acornote.databinding.FragmentSlidePageBinding
import tonnysunm.com.acornote.library.AndroidViewModelFactory
import tonnysunm.com.acornote.model.EmptyId
import tonnysunm.com.acornote.model.Image
import tonnysunm.com.acornote.model.Note
import tonnysunm.com.acornote.ui.HomeActivity
import tonnysunm.com.acornote.ui.label.LabelListActivity

class NoteFragment : Fragment() {

    private var binding: FragmentNoteBinding? = null
    private var menu: Menu? = null

    private val id by lazy {
        val id = activity?.intent?.getIntExtra("id", EmptyId)
        if (id != null && id > 0) id else null
    }

    private var noteBeforeEditing: Note? = null

    val viewModel by viewModels<NoteViewModel> {
        AndroidViewModelFactory(requireActivity().application, requireActivity().intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragment = this

        val binding = FragmentNoteBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = fragment
            viewModel = fragment.viewModel

            if (id == null) {
                titleView.requestFocus()
            }

            editLabel = View.OnClickListener {
                val startForResult =
                    requireActivity().prepareCall(ActivityResultContracts.StartActivityForResult()) {
                        if (it.resultCode == AppCompatActivity.RESULT_OK) {
                        }
                    }

                startForResult(Intent(context, LabelListActivity::class.java).apply {
                    putExtra("id", fragment.viewModel.data.value?.note?.id)
                })
            }

            editColor = View.OnClickListener {

            }
        }

        viewModel.data.observe(viewLifecycleOwner, Observer {
            val note = it.note
            if (it != null) { //been deleted
                if (note.id != 0 && this.noteBeforeEditing == null) {
                    this.noteBeforeEditing = note.copy()
                }

                updateMenuItems(this.menu, note)

                if (it.hasImage) {
                    binding.viewPager.adapter =
                        ImagePageViewAdapter(requireActivity(), it.images ?: listOf())

                    TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->

                    }.attach()
                }
            }
        })

        setHasOptionsMenu(true)

        requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                insertOrUpdateNote()
            }
        })

        this.binding = binding
        return binding.root
    }

    private fun updateMenuItems(menu: Menu?, note: Note?) {
        if (menu == null || note == null) return

        val star = menu.findItem(R.id.action_star)
        star?.setIcon(if (note.star == true) R.drawable.ic_stared else R.drawable.ic_star)

        val pin = menu.findItem(R.id.action_pin)
        pin?.setIcon(if (note.pinned == true) R.drawable.ic_pinned else R.drawable.ic_pin)

        menu.findItem(R.id.action_edit)?.isVisible = note.id != EmptyId
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.note, menu)

        this.menu = menu
        updateMenuItems(menu, viewModel.data.value?.note)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_star -> {
            val note = viewModel.data.value?.note
            note?.star = note?.star != true
            if (note?.id == 0) {
                updateMenuItems(this.menu, note)
            } else {
                viewModel.viewModelScope.launch(Dispatchers.IO) {
                    viewModel.updateNote()
                }
            }
            true
        }
        R.id.action_pin -> {
            val note = viewModel.data.value?.note
            note?.pinned = note?.pinned != true
            if (note?.id == 0) {
                updateMenuItems(this.menu, note)
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
        R.id.action_edit -> {
            viewModel.data.value?.note?.editing = true
            binding?.invalidateAll()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    fun insertOrUpdateNote() {

        val note = viewModel.data.value?.note ?: return

        val isInsert = this.id == null
        val inUpdate = this.noteBeforeEditing != note

        if (!isInsert && !inUpdate) {
            activity?.setResult(Activity.RESULT_CANCELED)
            activity?.finish()
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                if (isInsert) {
                    HomeActivity.scrollToTop = isInsert
                }

                note.editing = note.title.isEmpty()
                viewModel.updateNote()

                activity?.setResult(Activity.RESULT_OK)
            } catch (e: Exception) {
                activity?.setResult(Activity.RESULT_CANCELED)
            } finally {
                activity?.finish()
            }
        }
    }

    inner class ImagePageViewAdapter(fa: FragmentActivity, private val images: List<Image>) :
        FragmentStateAdapter(fa) {

        override fun getItemCount() = images.size ?: 0

        override fun createFragment(position: Int) = ImageSlidePageFragment(images[position])
    }

    class ImageSlidePageFragment(val image: Image) : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            val binding = FragmentSlidePageBinding.inflate(inflater, container, false).apply {
                data = image
            }

            return binding.root
        }
    }
}

