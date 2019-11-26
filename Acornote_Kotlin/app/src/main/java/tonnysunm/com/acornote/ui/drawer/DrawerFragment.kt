package tonnysunm.com.acornote.ui.drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.forEach
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
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


        mViewModel.allNotesCountLiveData.observe(viewLifecycleOwner, Observer {
            val item = binding.navView.menu.findItem(R.id.nav_all)

            val textView = item.actionView.findViewById<TextView>(R.id.notes_count)
            textView.text = it.toString()
        })

        mViewModel.favouriteCountLiveData.observe(viewLifecycleOwner, Observer {
            val item = binding.navView.menu.findItem(R.id.nav_favourite)

            val textView = item.actionView.findViewById<TextView>(R.id.notes_count)
            textView.text = it.toString()
        })

        mViewModel.data.observe(viewLifecycleOwner, Observer {

            val itemIds = mutableListOf<Int>()
            binding.navView.menu.forEach { item ->
                if (item.groupId == R.id.menu_group_folders &&
                    item.itemId != R.id.menu_group_folders_title
                ) {
                    itemIds.add(item.itemId)
                }
            }

            itemIds.forEach { id ->
                binding.navView.menu.removeItem(id)
            }

            //
            it.forEachIndexed { index, folderWrapper ->
                val item = binding.navView.menu.add(
                    R.id.menu_group_folders,
                    folderWrapper.folder.id.toInt(),
                    index,
                    folderWrapper.folder.title // + "_" + folderWrapper.folder.id + "_" + folderWrapper.noteCount
                ).setActionView(R.layout.drawer_item)
                    .setCheckable(true)

                val textView = item.actionView.findViewById<TextView>(R.id.notes_count)
                textView.text = folderWrapper.noteCount.toString()
            }

            binding.navView.invalidate()
        })

        binding.setOnAddFolder {
            val drawer = activity?.findViewById(R.id.drawer_layout) as? DrawerLayout
            drawer?.closeDrawers()

            activity?.findNavController(R.id.nav_host_fragment)?.navigate(R.id.nav_edit_folder)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as? MainActivity)?.let {
            mainModel = ViewModelProvider(it).get(SharedViewModel::class.java)
        }

        val navView = view as? NavigationView
        navView?.setNavigationItemSelectedListener { item ->

            mainModel.noteFilterLiveData.value = when (item.itemId) {
                R.id.nav_all -> NoteFilter.All
                R.id.nav_favourite -> NoteFilter.Favourite
                else -> mViewModel.data.value?.find { it.folder.id.toInt() == item.itemId }?.folder?.run {
                    NoteFilter.ByFolder(id, title)
                }
            }

            //
            val drawer = activity?.findViewById(R.id.drawer_layout) as? DrawerLayout
            drawer?.closeDrawers()

            //
            val menu = navView.menu
            menu.forEach { menuItem ->
                menuItem.isChecked = menuItem == item
            }
            true
        }

    }

}
