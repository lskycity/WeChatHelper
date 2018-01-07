
package com.lskycity.support.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.lskycity.support.R;
import com.lskycity.support.utils.DensityUtils;
import com.lskycity.support.utils.ViewOutlineUtils;


/**
 * @author WendyLin
 * @since Oct 8, 2014
 */
public class RoundCornerLayout extends FrameLayout {
    private final Paint mPaint = new Paint();

    private final Path mPath = new Path();

    private float mRadius;

    public RoundCornerLayout(Context context) {
        this(context, null);
    }

    public RoundCornerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("NewApi")
    public RoundCornerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundCornerLayout);
        mRadius = a.getDimensionPixelSize(R.styleable.RoundCornerLayout_radius, DensityUtils.dpTopx(getContext(), 10));

        a.recycle();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setOutlineProvider(new ViewOutlineUtils.RoundRectOutlineProvider(mRadius));
            setClipToOutline(true);
        } else {
            mPaint.setAntiAlias(true);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

            mPath.setFillType(Path.FillType.INVERSE_WINDING);

            setWillNotDraw(false);
        }

    }

    @SuppressLint("NewApi")
    public void setRadius(float radius) {
        if (mRadius != radius) {
            mRadius = radius;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setOutlineProvider(new ViewOutlineUtils.RoundRectOutlineProvider(mRadius));
                setClipToOutline(true);
            } else {
                resetPath();
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            invalidateOutline();
        } else {
            resetPath();
        }
    }

    private void resetPath() {
        mPath.reset();
        mPath.addRoundRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()), mRadius, mRadius,
                Path.Direction.CW);
    }

    @Override
    public void draw(Canvas canvas) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.draw(canvas);
        } else {
            canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
            super.draw(canvas);
            canvas.drawPath(mPath, mPaint);
            canvas.restore();
        }
    }


}
