package com.yn.reader.network.api;


/**
 * Created by sunxy
 * on 15/5/6.
 */
public interface Callback {

    void onSuccess(Object o);

    void onFailure(int code, String msg);

    void onCompleted();
}