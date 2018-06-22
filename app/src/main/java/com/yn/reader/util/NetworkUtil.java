package com.yn.reader.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.yn.reader.MiniApp;


/**
 * Created by sunxiangyao on 15/2/8.
 */
public class NetworkUtil {
    public static final int TYPE_WIFI = 0, TYPE_MOBILE = 1, TYPE_NOT_CONNECTED = 2;
    public static int currentNetworkType = TYPE_MOBILE;

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return TYPE_MOBILE;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) return TYPE_MOBILE;
        }

        return TYPE_NOT_CONNECTED;
    }

    public static void refreshNetworkType(Context context) {
        currentNetworkType = NetworkUtil.getConnectivityStatus(context);
    }

    public static boolean isWifiConnected() {
        return getConnectivityStatus(MiniApp.getInstance()) == TYPE_WIFI;
    }

    public static boolean isNetworkConnected() {
        return getConnectivityStatus(MiniApp.getInstance()) != TYPE_NOT_CONNECTED;
    }
}
