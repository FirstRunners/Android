package com.android.firstlearners.learners.etc;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class CustomProgressBar extends ProgressBar {
    private String text;
    private Paint textPaint;

    public CustomProgressBar(Context context) {
        super(context);
        setTextDefault();
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTextDefault();
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTextDefault();
    }


    private void setTextDefault(){
        text="0%";
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
    }

    public void setText(String text){
        this.text = text;
    }
}
