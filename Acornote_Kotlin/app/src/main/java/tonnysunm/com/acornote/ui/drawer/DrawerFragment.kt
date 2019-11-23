package tonnysunm.com.acornote.ui.drawer

import android.os.Bundle
import android.view.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import tonnysunm.com.acornote.ui.drawer.DrawerViewModel
import tonnysunm.com.acornote.R
import tonnysunm.com.acornote.databinding.FragmentDrawerBinding


class DrawerFragment : Fragment() {

    private val viewModel: DrawerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDrawerBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = this.viewModel

        viewModel.data.observe(viewLifecycleOwner, Observer {

            it.forEach { folderWrapper ->
                binding.navView.menu.add(
                    R.id.menu_group_folders,
                    R.id.nav_folder,
                    Menu.NONE,
                    folderWrapper.folder.title + "_" + folderWrapper.noteCount
                )
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

        val navView = view as? NavigationView
        activity?.let {
            navView?.setupWithNavController(it.findNavController(R.id.nav_host_fragment))
        }
    }

}
