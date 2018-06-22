package com.yn.reader.statusbar;

import android.view.Window;

/**
 * 状态栏接口
 *
 */

interface IStatusBar {
    void setStatusBarColor(Window window, boolean fullscreen, int color);
}
