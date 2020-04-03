package tonnysunm.com.acornote

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import tonnysunm.com.acornote.ui.drawer.DrawerViewModel
import tonnysunm.com.acornote.ui.note.edit.EditNoteActivity
import tonnysunm.com.acornote.ui.note.edit.EditNoteViewModel
import tonnysunm.com.acornote.ui.note.edit.EditNoteViewModelFactory


class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        //
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //set drawer icon
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(DrawerArrowDrawable(this))

        //
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, EditNoteActivity::class.java).apply {
                //val noteFilter = mViewModel.noteFilterLiveData.value
                //putExtra(getString(R.string.labelIdKey), noteFilter?.labelId)

//                if (noteFilter == NoteFilter.Star) {
//                    putExtra("star", true)
//                }
            }
            startActivity(intent)
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
