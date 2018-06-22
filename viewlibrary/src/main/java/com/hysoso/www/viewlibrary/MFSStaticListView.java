package com.hysoso.www.viewlibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by admin on 2017/1/20.
 */

public class MFSStaticListView extends ListView {
    public MFSStaticListView(Context context) {
        super(context);
    }

    public MFSStaticListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MFSStaticListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
