package com.hysoso.www.viewlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by luhe on 2017/11/11
 * email 1019561500@qq.com
 */

public class RoundedCornerTextView extends android.support.v7.widget.AppCompatTextView {


    private Context mContext;
    private int mBgColor = 0;
    private int mCornerSize = 0;

    public RoundedCornerTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getAttrs(context, attrs);
    }

    public RoundedCornerTextView(Context context) {
        super(context);
    }

    public RoundedCornerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context, attrs);
        mContext = context;
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        setBackgroundRounded(canvas, this.getMeasuredWidth(), this.getMeasuredHeight(), this);
        super.onDraw(canvas);
    }

    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.round_textview);
        mBgColor = ta.getColor(R.styleable.round_textview_bgColor, Color.TRANSPARENT);
        mCornerSize = (int)ta.getDimension(R.styleable.round_textview_bgColor, 3);
        ta.recycle();
    }

    public void setBackgroundRounded(Canvas c, int w, int h, View v){
        if(w <= 0 || h <= 0){
            return;
        }
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        paint.setAntiAlias(true);
        paint.setColor(mBgColor);

        RectF rec = new RectF(0, 0, w, h);
        c.drawRoundRect(rec, mCornerSize, mCornerSize, paint);
    }
    public void setBgColor(int color){
        mBgColor = color;
//        invalidate();
    }
    public void setCornerSize(int cornerSize){
        mCornerSize = cornerSize;
//        invalidate();
    }
}