package com.yn.reader.login.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.hysoso.www.utillibrary.LogUtil;
import com.yn.reader.R;

/**
 * 操作进度框
 * Created by sunxy on 2018/3/15.
 */

public class LoadingUtils {
    public static void showLoading() {
//        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progress);
//        DoubleBounce doubleBounce = new DoubleBounce();
//        progressBar.setIndeterminateDrawable(doubleBounce);
    }

    public static void hideLoading() {

    }

    public static void hideLoading(View content) {
        View loadingView = content.findViewById(R.id.loadingView);
        loadingView.setVisibility(View.GONE);
    }

    public static void showLoading(View content) {
        View loadingView = content.findViewById(R.id.loadingView);
        loadingView.setVisibility(View.VISIBLE);
    }

    public static void hideLoading(Context context) {
        try {
            View loadingView = ((Activity) context).findViewById(R.id.loadingView);
            loadingView.setVisibility(View.GONE);
        } catch (Exception ignored) {
        }
    }

    public static void showLoading(Context context) {
        try {
            View loadingView = ((Activity) context).findViewById(R.id.loadingView);
            loadingView.setVisibility(View.VISIBLE);
        } catch (Exception ignored) {
        }
    }
}
