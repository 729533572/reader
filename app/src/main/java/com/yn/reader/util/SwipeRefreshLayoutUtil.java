package com.yn.reader.util;

import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by luhe on 2018/3/26.
 */

public class SwipeRefreshLayoutUtil {
    public static void styleSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_blue_light, android.R.color.holo_blue_dark);
    }
}
