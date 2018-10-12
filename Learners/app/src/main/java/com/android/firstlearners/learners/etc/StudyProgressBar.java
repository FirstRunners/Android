package com.android.firstlearners.learners.etc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class StudyProgressBar extends View {
    private Context mContext;
    private Paint normalPaint;
    private Paint activePaint;
    private Paint textPaint;
    private float scale;
    private int progress;

    public StudyProgressBar(Context context) {
        super(context);
        init(context);
    }

    public StudyProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StudyProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        progress = 0;
        scale = mContext.getResources().getDisplayMetrics().density;

        normalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        normalPaint.setColor(Color.parseColor("#ebebeb"));
        normalPaint.setStyle(Paint.Style.FILL);
        normalPaint.setStrokeWidth(20 * scale);

        activePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        activePaint.setColor(Color.parseColor("#ff4a4a"));
        activePaint.setStyle(Paint.Style.FILL);
        activePaint.setStrokeWidth(20 * scale);

        textPaint = new Paint();
        textPaint.setColor(Color.parseColor("#ff4a4a"));
        textPaint.setTextSize(12 * scale);
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
        float left = 0;
        float right = getMeasuredWidth();
        float top = 0;
        float bottom = 20 * scale;

        RectF rectF = new RectF(left, top, right, bottom);
        canvas.drawRoundRect(rectF, 100, 100, normalPaint);

        right = (getMeasuredWidth() * progress / 100.0f);
        RectF rectF1 = new RectF(left, top, right, bottom);
        canvas.drawRoundRect(rectF1, 100, 100, activePaint);

        left = left + (8 * scale);
        top = (top + bottom) / 2 + (4 * scale);
        if(progress > 11){
            textPaint.setColor(Color.WHITE);
            left = right - (25 * scale);
        }else if(progress > 0){
            textPaint.setColor(Color.WHITE);
        }

        canvas.drawText(progress + "%",left, top,textPaint);
    }

    public void setProgress(int progress){
        this.progress = progress;
        invalidate();
    }
}
