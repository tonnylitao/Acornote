package tonnysunm.com.acornote.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat

fun Activity.showSoftKeyboard(view: View) {
    if (view.requestFocus()) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}

fun Activity.hideSoftKeyboard(view: View? = currentFocus) {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

    if (view != null) {
        view.clearFocus()
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Context.getColorString(id: Int): String =
    String.format("#%06x", ContextCompat.getColor(this, id) and 0xffffff)

fun Context.getStringResourceByName(aString: String): String? {
    val resId: Int = resources.getIdentifier(aString, "string", packageName)
    return getString(resId)
}