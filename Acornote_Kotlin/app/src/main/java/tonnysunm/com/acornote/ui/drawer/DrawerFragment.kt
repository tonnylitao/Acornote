package tonnysunm.com.acornote.ui.drawer

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.AttributeSet
import android.view.*
import android.widget.TextView
import androidx.core.view.forEach
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import tonnysunm.com.acornote.ui.HomeSharedViewModel
import tonnysunm.com.acornote.R
import tonnysunm.com.acornote.databinding.FragmentDrawerBinding
import tonnysunm.com.acornote.model.NoteFilter


class DrawerFragment : Fragment() {

    private val mViewModel: DrawerViewModel by viewModels()

    private val homeSharedModel: HomeSharedViewModel by lazy {
        ViewModelProvider(requireActivity()).get(HomeSharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragment = this
        val binding = FragmentDrawerBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = fragment
            viewModel = fragment.mViewModel
        }

        mViewModel.allNotesCountLiveData.observe(viewLifecycleOwner, Observer {
            val item = binding.navView.menu.findItem(R.id.nav_all)

            val textView = item.actionView.findViewById<TextView>(R.id.notes_count)
            textView.text = it.toString()
        })

        mViewModel.starCountLiveData.observe(viewLifecycleOwner, Observer {
            val item = binding.navView.menu.findItem(R.id.nav_star)

            val textView = item.actionView.findViewById<TextView>(R.id.notes_count)
            textView.text = it.toString()
        })

        mViewModel.data.observe(viewLifecycleOwner, Observer {

            val itemIds = mutableListOf<Int>()
            binding.navView.menu.forEach { item ->
                if (item.groupId == R.id.menu_group_labels &&
                    item.itemId != R.id.menu_group_labels_title
                ) {
                    itemIds.add(item.itemId)
                }
            }

            itemIds.forEach { id ->
                binding.navView.menu.removeItem(id)
            }

            //
            it.forEachIndexed { index, labelWrapper ->
                val itemId = labelWrapper.label.id.toInt()

                val item = binding.navView.menu.add(
                    R.id.menu_group_labels,
                    itemId,
                    index,
                    labelWrapper.label.title // + "_" + labelWrapper.label.id + "_" + labelWrapper.noteCount
                ).setActionView(R.layout.drawer_item)
                    .setCheckable(true)

                homeSharedModel.noteFilterLiveData.value?.let { filter ->
                    item.isChecked = item.isChecked(filter)
                }

                val textView = item.actionView.findViewById<TextView>(R.id.notes_count)
                textView.text = labelWrapper.noteCount.toString()
            }

            binding.navView.invalidate()
        })

        val navView = binding.navView

        homeSharedModel.noteFilterLiveData.observe(viewLifecycleOwner, Observer {
            val drawer = activity?.findViewById(R.id.drawer_layout) as? DrawerLayout
            if (drawer?.isOpen == true) {
                drawer.closeDrawers()
            }

            updateMenuChecked(it)
        })

        navView.setNavigationItemSelectedListener { item ->

            homeSharedModel.noteFilterLiveData.value = item.noteFilter

            true
        }

        return binding.root
    }

    private fun updateMenuChecked(filter: NoteFilter) {
        val menu = (view as? NavigationView)?.menu
        menu?.forEach { menuItem ->
            menuItem.isChecked = menuItem.isChecked(filter)
        }
    }
}

private val MenuItem.noteFilter: NoteFilter
    get() = when (itemId) {
        R.id.nav_all -> NoteFilter.All
        R.id.nav_star -> NoteFilter.Star
        else -> NoteFilter.ByLabel(itemId.toLong(), title.toString())
    }

private fun MenuItem.isChecked(filter: NoteFilter) = when (itemId) {
    R.id.nav_all -> filter == NoteFilter.All
    R.id.nav_star -> filter == NoteFilter.Star
    else -> itemId == filter.labelId?.toInt()
}

class AllowChildInterceptTouchEventDrawerLayout(context: Context, attrs: AttributeSet) :
    DrawerLayout(context, attrs) {

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val drawer = findViewById(R.id.fragment_drawer) as? View
        val scroll = drawer?.findViewById(R.id.color_tag_fragment) as? View
        if (scroll != null) {
            val rect = Rect()
            scroll.getHitRect(rect)

            if (ev.y > rect.top) {
                return false
            }
        }
        return super.onInterceptTouchEvent(ev)
    }
}