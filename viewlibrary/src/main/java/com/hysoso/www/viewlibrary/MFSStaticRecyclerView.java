package com.hysoso.www.viewlibrary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by admin on 2017/1/20.
 */

public class MFSStaticRecyclerView extends RecyclerView {
    public MFSStaticRecyclerView(Context context) {
        super(context);
    }

    public MFSStaticRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MFSStaticRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
