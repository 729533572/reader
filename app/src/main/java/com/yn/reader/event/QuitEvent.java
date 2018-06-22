package com.yn.reader.event;

/**
 * 退出事件
 * Created by sunxy on 2018/3/26.
 */

public class QuitEvent {
    private boolean quit;
    public QuitEvent(boolean quit) {
        this.quit = quit;
    }

    public void setQuit(boolean quit) {
        this.quit = quit;
    }

    public boolean isQuit() {
        return quit;
    }
}
