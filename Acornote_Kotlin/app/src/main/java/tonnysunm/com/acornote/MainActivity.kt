package tonnysunm.com.acornote

import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
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

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_all,
                R.id.nav_favourite
//                R.id.nav_folder
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
//        findViewById<NavigationView>(R.id.nav_view).setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    //////

    private val viewModel: DrawerViewModel by viewModels()

    private val viewModel2: EditNoteViewModel by viewModels {
        EditNoteViewModelFactory(this.application, null, null)
    }

    private fun dev_createDemo() {
//        lifecycleScope.launch {
        //            viewModel2.updateOrInsertNote()
//        }
    }

}
