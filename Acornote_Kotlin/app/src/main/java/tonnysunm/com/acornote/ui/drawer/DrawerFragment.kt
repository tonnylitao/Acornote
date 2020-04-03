package tonnysunm.com.acornote.ui.drawer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import tonnysunm.com.acornote.MainActivity
import tonnysunm.com.acornote.R
import tonnysunm.com.acornote.SharedViewModel
import tonnysunm.com.acornote.databinding.FragmentDrawerBinding
import tonnysunm.com.acornote.model.NoteFilter


class DrawerFragment : Fragment() {

    private val mViewModel: DrawerViewModel by viewModels()

    private lateinit var mainModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDrawerBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = this.mViewModel

        (activity as? MainActivity)?.let {
            mainModel = ViewModelProvider(it).get(SharedViewModel::class.java)
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

                mainModel.noteFilterLiveData.value?.let { filter ->
                    item.isChecked = item.isChecked(filter)
                }

                val textView = item.actionView.findViewById<TextView>(R.id.notes_count)
                textView.text = labelWrapper.noteCount.toString()
            }

            binding.navView.invalidate()
        })

        binding.setOnAddLabel {
            val drawer = activity?.findViewById(R.id.drawer_layout) as? DrawerLayout
            drawer?.closeDrawers()

//            activity?.findNavController(R.id.nav_host_fragment)?.navigate(R.id.nav_edit_label)
        }

        val navView = binding.navView


        mainModel.noteFilterLiveData.observe(viewLifecycleOwner, Observer {
            updateMenuChecked(it)
        })

        navView.setNavigationItemSelectedListener { item ->

            mainModel.noteFilterLiveData.value = item.noteFilter

            //
            val drawer = activity?.findViewById(R.id.drawer_layout) as? DrawerLayout
            drawer?.closeDrawers()

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