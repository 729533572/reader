package com.yn.reader.util;

/**
 * Created by luhe on 2018/5/28.
 */

public class AlipayManager {
    private static AlipayManager mInstance;
    private static String App_id = "2018052360228252";

    public static AlipayManager getInstance() {
        if (mInstance == null) {
            synchronized (AlipayManager.class) {
                mInstance = new AlipayManager();
            }
        }
        return mInstance;
    }

    private AlipayManager() {

    }
}
