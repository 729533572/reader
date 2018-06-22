package com.yn.reader.util;

import android.util.Log;

import com.yn.reader.BuildConfig;


/**
 * Created by sunxiangyao on 15/2/14.
 */
public class LogUtils {
    public static final String TAG = "Welike";
    public static final String TAG_SUNXY = "sunxy";

    public static void log(String msg) {
        if (BuildConfig.DEBUG) Log.d(TAG, msg);
    }
    public static void sunxy(String msg) {
        if (BuildConfig.DEBUG) Log.d(TAG_SUNXY, msg);
    }
}
