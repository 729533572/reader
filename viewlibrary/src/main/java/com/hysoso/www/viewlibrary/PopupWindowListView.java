package com.hysoso.www.viewlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by 陆贺 on 2018/2/8
 * email 1019561500@qq.com
 */

public class PopupWindowListView extends ListView{
    private String TAG = getClass().getSimpleName();
    private int maxHeight;
    private int maxWidth;
    public PopupWindowListView(Context context) {
        super(context);
    }

    public PopupWindowListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PopupWindowListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PopupWindowListView,
                defStyleAttr,
                0
        );
        for (int i = 0; i < typedArray.length(); i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.PopupWindowListView_maxHeight) {
                maxHeight = typedArray.getDimensionPixelOffset(attr, 0);

            } else if (attr == R.styleable.PopupWindowListView_maxWidth) {
                maxWidth = typedArray.getDimensionPixelSize(attr, 0);

            }
        }
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
            case MeasureSpec.AT_MOST:
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(maxWidth==0?widthSize:maxWidth,
                        widthSize),widthMode);
                break;
            case MeasureSpec.UNSPECIFIED:
                // Do nothing
                break;
        }

        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
            case MeasureSpec.AT_MOST:
                int i = maxHeight==0?heightSize:maxHeight;
                int height = Math.min(i,
                        heightSize);
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,heightMode);
                break;
            case MeasureSpec.UNSPECIFIED:
                // Do nothing
                break;
        }
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        invalidate();
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        invalidate();
    }
}
