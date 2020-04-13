package tonnysunm.com.acornote.ui.colortag

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import tonnysunm.com.acornote.R

class ColorTagView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var _colorString = context
        .obtainStyledAttributes(attrs, R.styleable.ColorTagView, 0, 0)
        .getString(R.styleable.ColorTagView_colorString)

    private val paint = Paint().apply {
        color = Color.parseColor(_colorString ?: "#000000")
    }

    var colorString: String?
        get() = _colorString
        set(value) {
            _colorString = value
            paint.color = Color.parseColor(value ?: "#000000")
        }

    override fun onDraw(canvas: Canvas) {
        val r = width.toFloat() / 2
        canvas.drawCircle(r, r, r, paint)
    }
}
