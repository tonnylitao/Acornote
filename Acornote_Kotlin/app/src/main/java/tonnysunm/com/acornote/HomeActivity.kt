package tonnysunm.com.acornote

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.activity.invoke
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import tonnysunm.com.acornote.model.NoteFilter
import tonnysunm.com.acornote.ui.note.NoteActivity

class HomeActivity : AppCompatActivity(R.layout.activity_main) {

    companion object {
        var scrollToTop = false
    }

    private val homeSharedModel: HomeSharedViewModel by lazy {
        ViewModelProvider(this).get(HomeSharedViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //set drawer icon
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(DrawerArrowDrawable(this))

        //fab
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {

            val startForResult = prepareCall(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                }
            }

            startForResult(Intent(this, NoteActivity::class.java).apply {
                when (val noteFilter = homeSharedModel.noteFilterLiveData.value) {
                    NoteFilter.Star ->
                        putExtra(getString(R.string.starKey), true)
                    is NoteFilter.ByLabel ->
                        putExtra(getString(R.string.labelIdKey), noteFilter.labelId)
                }

            })
        }

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

    private fun dev_createDemo() {
//        lifecycleScope.launch {
        //            viewModel2.updateOrInsertNote()
//        }
    }
}
