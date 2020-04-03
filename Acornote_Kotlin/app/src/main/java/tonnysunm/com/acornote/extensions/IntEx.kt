package tonnysunm.com.acornote.extensions

import android.content.res.Resources

val Int.dp: Int
    get() = if (this == 0) 0 else (this * Resources.getSystem().displayMetrics.density).toInt()