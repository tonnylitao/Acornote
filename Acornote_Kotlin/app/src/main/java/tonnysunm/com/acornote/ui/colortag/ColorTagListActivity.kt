package tonnysunm.com.acornote.ui.colortag

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_colortags.*
import kotlinx.android.synthetic.main.activity_labels.toolbar
import tonnysunm.com.acornote.R

class ColorTagListActivity : AppCompatActivity(R.layout.activity_colortags) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        saveColorTags()

        setResult(Activity.RESULT_CANCELED)
        finish()
        
        return true
    }

    override fun onBackPressed() {
        saveColorTags()
        super.onBackPressed()
    }

    private fun saveColorTags() {
        val fragment = fragment_edit_colortag as? ColorTagListFragment
        fragment?.viewModel?.saveColorTags()
    }

}
