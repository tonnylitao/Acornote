package tonnysunm.com.acornote.service

import android.content.Intent
import android.graphics.PixelFormat
import android.graphics.Point
import android.util.Log
import android.view.*
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.JobIntentService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tonnysunm.com.acornote.R
import tonnysunm.com.acornote.ui.popup.PopupActivity
import kotlin.math.roundToInt

class BubbleService : JobIntentService() {
    private var mWindowManager: WindowManager? = null
    private var mOverlayView: View? = null
    var mWidth = 0
    private var counterFab: ImageView? = null
    var activity_background = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            activity_background = intent.getBooleanExtra("activity_background", false)
        }
        if (mOverlayView == null) {
            mOverlayView = LayoutInflater.from(this).inflate(R.layout.overlay_bubble, null)
            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )

            //Specify the view position
            params.gravity = Gravity.LEFT
            params.x = 0
            params.y = 100

            mWindowManager = getSystemService(WINDOW_SERVICE) as? WindowManager
            mWindowManager?.addView(mOverlayView, params)

            val display = mWindowManager?.defaultDisplay
            val size = Point()
            display?.getSize(size)
            counterFab = mOverlayView?.findViewById(R.id.fabHead) as? ImageView

            val layout = mOverlayView?.findViewById(R.id.overlay_layout) as? View
            val vto = layout?.viewTreeObserver
            vto?.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    layout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val width = layout.measuredWidth

                    //To get the accurate middle of the screen we subtract the width of the floating widget.
                    mWidth = size.x - width
                }
            })

            counterFab?.setOnTouchListener(object : View.OnTouchListener {
                private var initialX = 0
                private var initialY = 0
                private var initialTouchX = 0f
                private var initialTouchY = 0f

                override fun onTouch(v: View?, event: MotionEvent): Boolean {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {

                            //remember the initial position.
                            initialX = params.x
                            initialY = params.y


                            //get the touch location
                            initialTouchX = event.rawX
                            initialTouchY = event.rawY
                            return true
                        }
                        MotionEvent.ACTION_UP -> {
                            val intent = Intent(
                                getBaseContext(),
                                PopupActivity::class.java
                            )
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            return true

                            //Only start the activity if the application is in background. Pass the current badge_count to the activity
                            if (activity_background) {
                                val xDiff = event.rawX - initialTouchX
                                val yDiff = event.rawY - initialTouchY
                                if (Math.abs(xDiff) < 5 && Math.abs(yDiff) < 5) {


                                    //close the service and remove the fab view
                                }
                                stopSelf()
                            }

                            //Logic to auto-position the widget based on where it is positioned currently w.r.t middle of the screen.
                            val middle = mWidth / 2
                            val nearestXWall =
                                (if (params.x >= middle) mWidth else 0).toFloat()
                            params.x = nearestXWall.toInt()
                            mWindowManager!!.updateViewLayout(mOverlayView, params)
                            return true
                        }
                        MotionEvent.ACTION_MOVE -> {
                            val xDiff2 = (event.rawX - initialTouchX).roundToInt()
                            val yDiff2 = (event.rawY - initialTouchY).roundToInt()


                            //Calculate the X and Y coordinates of the view.
                            params.x = initialX + xDiff2
                            params.y = initialY + yDiff2

                            //Update the layout with new X & Y coordinates
                            mWindowManager!!.updateViewLayout(mOverlayView, params)
                            return true
                        }
                    }
                    return false
                }
            })
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onHandleWork(intent: Intent) {
        val txt = intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT) ?: return

        Log.d("TAG", "service $txt")

        val ctx = this
        GlobalScope.launch(Dispatchers.Main) {
            val textView = mOverlayView?.findViewById(R.id.number) as? TextView
            textView?.text = "2"
        }
    }

    override fun onCreate() {
        super.onCreate()
        setTheme(R.style.AppTheme)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mOverlayView != null) mWindowManager!!.removeView(mOverlayView)
    }
}