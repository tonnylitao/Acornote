package tonnysunm.com.acornote

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import tonnysunm.com.acornote.ui.drawer.DrawerViewModel
import tonnysunm.com.acornote.ui.note.EditNoteViewModel
import tonnysunm.com.acornote.ui.note.EditNoteViewModelFactory


/*TODO


*/

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

//        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

//        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
//        val navController = findNavController(R.id.nav_host_fragment)
//        appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.nav_all,
//                R.id.nav_favourite
////                R.id.nav_label //for isTopLevelDestination = NavigationUI.matchDestinations(destination,
//            ), drawerLayout
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        fun navigateUp(): Boolean {
            val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
            val navController = findNavController(R.id.nav_host_fragment)

            val currentDestination = navController.currentDestination ?: return false

            return if (R.id.nav_label == currentDestination.id) {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            } else {
                navController.navigateUp()
            }
        }

        return navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            super.onBackPressed()
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }


    //////

    private val viewModel: DrawerViewModel by viewModels()

    private val viewModel2: EditNoteViewModel by viewModels {
        EditNoteViewModelFactory(this.application, null)
    }

    private fun dev_createDemo() {
//        lifecycleScope.launch {
        //            viewModel2.updateOrInsertNote()
//        }
    }
}
