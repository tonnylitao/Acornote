package tonnysunm.com.acornote.ui.drawer

import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.core.os.bundleOf
import androidx.core.view.forEach
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView
import tonnysunm.com.acornote.R
import tonnysunm.com.acornote.databinding.FragmentDrawerBinding
import java.lang.ref.WeakReference


class DrawerFragment : Fragment() {

    private val viewModel: DrawerViewModel by viewModels()

    private val mArrowDrawable: DrawerArrowDrawable by lazy {
        DrawerArrowDrawable(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDrawerBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = this.viewModel

        viewModel.data.observe(viewLifecycleOwner, Observer {
            it.forEachIndexed { index, folderWrapper ->
                binding.navView.menu.add(
                    R.id.menu_group_folders,
                    R.id.nav_folder,
                    index,
                    folderWrapper.folder.title + "_" + folderWrapper.folder.id + "_" + folderWrapper.noteCount
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

        val navController = activity?.findNavController(R.id.nav_host_fragment) ?: return

        val navView = view as? NavigationView
        navView?.setNavigationItemSelectedListener {

            onNavDestinationSelected(it, navController)

            val drawer = activity?.findViewById(R.id.drawer_layout) as? DrawerLayout
            drawer?.closeDrawers()

            true
        }

        val weekNavView = WeakReference<NavigationView>(navView)
        navController?.addOnDestinationChangedListener { _, destination, arguments ->
            val navView = weekNavView.get() ?: return@addOnDestinationChangedListener

            val menu = navView.menu
            menu.forEach {
                val selected = when (val id = it.itemId) {
                    R.id.nav_all, R.id.nav_favourite -> destination.id == id
                    R.id.nav_folder -> viewModel.data.value?.get(it.order)?.folder?.id == arguments?.getLong(
                        getString(R.string.folderIdKey)
                    )
                    else -> false
                }
                it.isChecked = selected
            }

            if (destination.id == R.id.nav_folder) {
                //action bar title
                val actionBar = (activity as AppCompatActivity).supportActionBar
                val label = destination.label
                if (TextUtils.isEmpty(label)) {
                    actionBar?.title =
                        arguments?.getString("folderTitle")
                }

                //action bar icon
                actionBar?.setDisplayHomeAsUpEnabled(true)
                val delegate = (activity as AppCompatActivity).drawerToggleDelegate
                delegate?.setActionBarUpIndicator(
                    mArrowDrawable,
                    androidx.navigation.ui.R.string.nav_app_bar_open_drawer_description
                )
                mArrowDrawable.progress = 0f
            }

        }
    }

    private fun onNavDestinationSelected(item: MenuItem, navController: NavController): Boolean {
        val builder = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
            .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
            .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
        builder.setPopUpTo(R.id.nav_all, true)

        return try {
            when (item.itemId) {
                R.id.nav_all, R.id.nav_favourite -> navController.navigate(
                    item.itemId,
                    null,
                    builder.build()
                )
                R.id.nav_folder -> {
                    navController.navigate(
                        R.id.nav_folder,
                        bundleOf(
                            getString(R.string.folderIdKey) to viewModel.data.value?.get(item.order)?.folder?.id,
                            "folderTitle" to viewModel.data.value?.get(item.order)?.folder?.title
                        ),
                        builder.build()
                    )
                }
            }
            true
        } catch (e: IllegalArgumentException) {
            Log.d("TAG", e.toString())
            false
        }
    }

}
