package com.yn.reader.util;

import android.os.Handler;
import android.os.Looper;

import com.hysoso.www.utillibrary.LogUtil;
import com.squareup.otto.Bus;

public class BusProvider extends Bus {
    private static BusProvider BUS;
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public static BusProvider getInstance() {
        if (BUS == null) BUS = new BusProvider();
        return BUS;
    }

    private BusProvider() {
        // No instances.
    }

    @Override
    public void post(final Object event) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                super.post(event);
            } else {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        BusProvider.super.post(event);
                    }
                });
            }
        }catch (Exception ex){
            LogUtil.e(getClass().getSimpleName(),ex.getMessage()+"/"+ex.getCause());
        }
    }
}
