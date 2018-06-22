package com.yn.reader.util;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;


/**
 * 倒计时
 * Created by sunxy on 2018/1/19.
 */

public class RxCountDown {
    public static Observable<Integer> countdown(int time) {
        if (time < 0) time = 0;

        final int countTime = time;
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(Long increaseTime) throws Exception {
                        return countTime - increaseTime.intValue();
                    }
                })
                .take(countTime + 1);

    }

}
