package com.android.firstlearners.learners.etc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.android.firstlearners.learners.R;

public class CustomProgressBar extends View {
    private int mSize;
    private Paint mPaint;
    private RectF mRect;
    private float mIndeterminateSweep;
    private float mstartAngle;
    private Context mContext;
    private int progress;
    private Bitmap bitmap;
    public CustomProgressBar(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        progress = 0;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#ebebeb"));
        mPaint.setStyle(Paint.Style.FILL);
        float scale = mContext.getResources().getDisplayMetrics().density;
        mPaint.setStrokeWidth(25 * scale);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.shoes_1);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float scale = mContext.getResources().getDisplayMetrics().density;
        float left = 0;
        float right = getMeasuredWidth() - getPaddingRight();
        float bottom = getBottom();
        float top = bottom - (25*scale);
        RectF rectF = new RectF(left, top, right, bottom);


        float otherRight = (getMeasuredWidth() - getPaddingRight()) * progress / 100;

        RectF rectF1 = new RectF(left, top, otherRight, bottom);
        canvas.drawRoundRect(rectF, 100, 100, mPaint);
        mPaint.setColor(Color.RED);
        canvas.drawRoundRect(rectF1, 100, 100, mPaint);

        if(progress != 0){
            top = top - (9 * scale);
            otherRight = otherRight - (45 * scale);
            canvas.drawBitmap(bitmap,otherRight,top,null);
        }
    }

    public void setProgress(int progress){
        this.progress = progress;
        invalidate();
    }
}
