package com.yn.reader.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yn.reader.model.common.Book;
import com.yn.reader.util.ComUtils;

import java.util.List;

/**
 * 八宫格图片展示
 * Created by sunxy.
 */
public class EightPicLayout extends ViewGroup {
    private static final int TOTAL_COUNT = 8;
    private static final int HALF_TOTAL_COUNT = 4;
    private int horizontalSpace;
    private int verticalSpace;
    protected int picWidth;
    private int picHeight;
    private int startX;

    public EightPicLayout(Context context) {
        super(context);
    }

    public EightPicLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EightPicLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EightPicLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        horizontalSpace = ComUtils.dip2px(16);
        picHeight = ComUtils.dip2px(148);
        startX = ComUtils.dip2px(16);
        verticalSpace = ComUtils.dip2px(8);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LinearLayout.LayoutParams myselfLayoutParams = (LinearLayout.LayoutParams) getLayoutParams();
        int width = MeasureSpec.getSize(widthMeasureSpec)
                - myselfLayoutParams.leftMargin
                - myselfLayoutParams.rightMargin;

        picWidth = getPicWidth(width);
        int widthUsed = 0;
        int heightUsed = 0;
        int count = Math.min(getChildCount(), TOTAL_COUNT);
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == VISIBLE) {
                if (i % HALF_TOTAL_COUNT == 0) {
                    heightUsed += (picHeight + verticalSpace);
                }
                child.getLayoutParams().width = picWidth;
                child.getLayoutParams().height = picHeight;
                measureChildWithMargins(child, widthMeasureSpec, widthUsed, heightMeasureSpec, heightUsed);
                widthUsed += picWidth + horizontalSpace;
            }
        }
        //setMeasureDimension
        setMeasuredDimension(widthUsed, heightUsed);
    }

    protected int getPicWidth(int width) {
        return (width - horizontalSpace * 5) / HALF_TOTAL_COUNT;
    }

    @Override
    protected void onLayout(boolean change, int l, int t, int r, int b) {
        if (change) {
            int childCount = getChildCount();
            int left = startX;
            int top = 0;
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (child.getVisibility() == VISIBLE) {
                    if (i % HALF_TOTAL_COUNT == 0) {
                        left = startX;
                        top = (picHeight + verticalSpace) * (i / HALF_TOTAL_COUNT);
                    }
                    child.layout(left, top, left + picWidth, top + picHeight);
                    left += picWidth + horizontalSpace;
                }
            }
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new NinePicLayoutParams(getContext(), attrs);
    }

    public void setData(List<Book> books) {
        int count = Math.min(books.size(),getChildCount());
        for(int i=0;i<count;i++){

        }
    }

    public static class NinePicLayoutParams extends MarginLayoutParams {

        public NinePicLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public NinePicLayoutParams(int width, int height) {
            super(width, height);
        }

        public NinePicLayoutParams(LayoutParams source) {
            super(source);
        }
    }

    public int getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }
}
