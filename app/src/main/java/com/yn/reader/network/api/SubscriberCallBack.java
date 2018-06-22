package com.yn.reader.network.api;


import com.yn.reader.MiniApp;
import com.yn.reader.util.NetUtil;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;


/**
 * Created by Administrator
 * on 2016/5/18.
 */
public class SubscriberCallBack extends DisposableObserver<Object> {
    private Callback apiCallback;

    public SubscriberCallBack(Callback apiCallback) {
        this.apiCallback = apiCallback;
    }

    @Override
    public void onNext(Object value) {
        if (!isDisposed())
            apiCallback.onSuccess(value);
    }


    @Override
    public void onComplete() {
        if (!isDisposed())
            apiCallback.onCompleted();
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (!isDisposed()) {
            try {
                String errorMsg = null;
                if (e instanceof HttpException) {

                    switch (((HttpException) e).code()) {
                        case 403:
                            errorMsg = "没有权限访问此链接！";
                            break;
                        case 504:
                            if (!NetUtil.isConnected(MiniApp.getInstance())) {
                                errorMsg = "没有联网哦！";
                            } else {
                                errorMsg = "网络连接超时！";
                            }
                            break;
                        default:
                            errorMsg = ((HttpException) e).message();
                            break;
                    }
                    apiCallback.onFailure(((HttpException) e).code(), errorMsg);

                } else {
                    apiCallback.onFailure(0, e.getMessage());
                }
                apiCallback.onCompleted();
            } catch (Exception exception) {

                //do
            }
        }
    }

}
