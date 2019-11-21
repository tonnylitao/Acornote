package tonnysunm.com.acornote

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import tonnysunm.com.acornote.model.EmptyId
import tonnysunm.com.acornote.ui.folder.EditFolderViewModel
import tonnysunm.com.acornote.ui.folder.EditFolderViewModelFactory

/*TODO


*/

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val viewModel1: EditFolderViewModel by viewModels {

        EditFolderViewModelFactory(
            application,
            null
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

//        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
//        findViewById<Button>(R.id.btn_all).setOnClickListener {
//            Log.d("TAG", "setOnClickListener")
//
//            drawerLayout.closeDrawer(navView)
//
//            val builder = NavOptions.Builder()
//                .setLaunchSingleTop(true)
//                .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
//                .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
//                .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
//                .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
//                .setPopUpTo(R.id.nav_all, true)
//
//            val options = builder.build()
//            navController.navigate(R.id.nav_all, null, options)
//        }
//
//        findViewById<Button>(R.id.btn_favourite).setOnClickListener {
//            Log.d("TAG", "setOnClickListener")
//
//            drawerLayout.closeDrawer(navView)
//
//            val builder = NavOptions.Builder()
//                .setLaunchSingleTop(true)
//                .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
//                .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
//                .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
//                .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
//                .setPopUpTo(R.id.nav_all, true)
//
//            val options = builder.build()
//            navController.navigate(R.id.nav_favourite, null, options)
//        }

        //
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_all,
                R.id.nav_favourite,
                R.id.nav_folder
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

//
//        lifecycleScope.launch {
//            val id = viewModel1.updateOrInsertFolder("Hello")
//
//
//            viewModel1.updateOrInsertFolder("Hello1")
//
//        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        Log.d("TAG", "onSupportNavigateUp")

        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}
