package com.yn.reader.widget;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 解决嵌套ViewPager冲突问题
 * Created by sunxy on 2018/2/7.
 */

public class NestViewPager extends ViewPager {
    PointF downF = new PointF();
    PointF curF = new PointF();

    public NestViewPager(Context context) {
        super(context);
    }

    public NestViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        curF.x = event.getX();
        curF.y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            downF.x = event.getX();
            downF.y = event.getY();
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            curF.x = event.getX();
            curF.y = event.getY();
            if (Math.abs(curF.x - downF.x) > Math.abs(curF.y - downF.y)) {
                if (curF.x > downF.x) { //右划
                    if (getCurrentItem() == 0) {//第一个页面，需要父控件拦截
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                } else {//左划
                    if (getCurrentItem() == getAdapter().getCount() - 1) {//最后一个页面，需要拦截
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
            } else {
                getParent().requestDisallowInterceptTouchEvent(false);
            }
        }
        return true;
    }

}