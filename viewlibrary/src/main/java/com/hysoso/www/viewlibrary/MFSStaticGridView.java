package com.hysoso.www.viewlibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by luhe on 17/3/17.
 */

public class MFSStaticGridView extends GridView{
    public MFSStaticGridView(Context context) {
        super(context);
    }

    public MFSStaticGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MFSStaticGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
