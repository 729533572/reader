package com.hysoso.www.utillibrary;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.TypedValue;

/**
 * Created by LuHe on 2016/11/10.
 */

public class DensityUtil {
    private DensityUtil()
    {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, float dpVal)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param context
     * @param spVal
     * @return
     */
    public static int sp2px(Context context, float spVal)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2dp(Context context, float pxVal)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2sp(Context context, float pxVal)
    {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

    public static Integer getWindowEditAreaHegit(Context context){
        Rect outRect = new Rect();
        ((Activity)context).getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        return outRect.height();
    }
    public static Integer getWindowWidth(Context context){
        Rect outRect = new Rect();
        ((Activity)context).getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        return outRect.width();
    }
    public static Integer getWindowHeight(Context context){
        Rect outRect = new Rect();
        ((Activity)context).getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        return outRect.height();
    }
    public static Integer getStatusBarHeight(Context context){
//        Rect frame = new Rect();
//        ((Activity)context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);

        int statusBarHeight1 = -1;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = context.getResources().getDimensionPixelSize(resourceId);
        }

        return statusBarHeight1;
    }
    public static Integer getActionBarHeight(Context context){
        TypedValue tv = new TypedValue();
        Integer actionBarHeight = null;
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return  actionBarHeight;
    }
}
