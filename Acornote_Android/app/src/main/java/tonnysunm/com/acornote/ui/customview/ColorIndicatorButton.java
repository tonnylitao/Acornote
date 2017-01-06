package tonnysunm.com.acornote.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;

import tonnysunm.com.acornote.R;
import tonnysunm.com.acornote.model.Folder;

/**
 * Created by Tonny on 6/01/17.
 */

public class ColorIndicatorButton extends AppCompatImageButton {
    private static final float DOT_RADIUS = 16;
    private static final float LARGE_WIDTH = DOT_RADIUS*4;
    private static final float BORDER = 10;

    private int mColor;
    private boolean mSelected;

    private Paint mPaint;
    private Paint mOvalPaint;

    public ColorIndicatorButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.ColorIndicatorButton, 0, 0);

        try {
            mColor = Folder.colorByIndex(context, a.getInteger(R.styleable.ColorIndicatorButton_colorIndex, 0));
            mSelected = a.getBoolean(R.styleable.ColorIndicatorButton_selected, true);
        } finally {
            a.recycle();
        }

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mColor);

        mOvalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOvalPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void setSelected(boolean selected) {
        mSelected = selected;

        invalidate();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final float w = getWidth();
        final float h = getHeight();
        if (!mSelected) {
            canvas.drawCircle(w/2, h/2, DOT_RADIUS, mPaint);
        }else {
            final float left = w/2-LARGE_WIDTH/2, top = h/2-DOT_RADIUS;
            final float rx = DOT_RADIUS;

            mOvalPaint.setColor(Color.WHITE);
            canvas.drawRoundRect(left-BORDER, top-BORDER, w-left+BORDER, h-top+BORDER, rx+BORDER, rx+BORDER, mOvalPaint);

            mOvalPaint.setColor(mColor);
            canvas.drawRoundRect(left, top, w-left, h-top, rx, rx, mOvalPaint);
        }
    }
}
