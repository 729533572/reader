package com.yn.reader.util;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yn.reader.R;


/**
 * Created by sunxy on 18/1/254
 */
public class ToolbarUtils {
    public static void styleToolBar(final AppCompatActivity activity, Toolbar toolbar, ImageView mTitleBack, TextView titleView, String title) {
        styleToolbar(activity, toolbar, mTitleBack, titleView, title, null);
    }

    public static void styleToolbar(final AppCompatActivity activity, Toolbar toolbar, ImageView mTitleBack, TextView titleView, String title, View.OnClickListener finishListener) {
        titleView.setText(title);
        if (finishListener == null) {
            toolbar.findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                }
            });
        } else {
            toolbar.findViewById(R.id.back_layout).setOnClickListener(finishListener);
        }
    }

}
