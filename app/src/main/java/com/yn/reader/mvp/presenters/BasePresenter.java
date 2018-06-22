package com.yn.reader.mvp.presenters;


import android.text.TextUtils;

import com.socks.library.KLog;
import com.yn.reader.login.utils.LoadingUtils;
import com.yn.reader.login.utils.ToastUtils;
import com.yn.reader.mvp.views.BaseView;
import com.yn.reader.network.api.Callback;
import com.yn.reader.network.api.SubscriberCallBack;
import com.yn.reader.model.BaseData;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


/**
 * 接收数据回调
 * Created by sunxiangyao on 15/4/5.
 */
public abstract class BasePresenter implements Callback {
    CompositeDisposable mCompositeSubscription;

    /**
     * 初始化MVPView
     */
    public abstract BaseView getBaseView();

    public abstract void success(Object o);

    public abstract void detachView();

    @Override
    public void onSuccess(Object o) {
        if (getBaseView() != null) {
            if (o != null && getBaseView().getContext() != null) {
                if(o instanceof BaseData) {
                    BaseData baseData = ((BaseData) o);
                    if (baseData.getStatus() == 1) {
                        success(o);
                    } else if (!TextUtils.isEmpty(baseData.getErr_msg())) {
                        ToastUtils.showLong(getBaseView().getContext(), baseData.getErr_msg());
                    }
                }else{
                    success(o);
                }
            }
        }
    }

    @Override
    public void onFailure(int code, String msg) {
        KLog.d("错误码" + code + "----->" + msg);
        LoadingUtils.hideLoading(getBaseView().getContext());
    }


    @Override
    public void onCompleted() {

    }

    //RXjava取消注册，以避免内存泄露
    void unSubscribe() {
        if (mCompositeSubscription != null && !mCompositeSubscription.isDisposed()) {
            mCompositeSubscription.dispose();
        }
    }


    public void addSubscription(Observable<?> observable) {
        addSubscription(observable, new SubscriberCallBack(this));
    }

    public void addSubscription(Observable<?> observable, Callback callback) {
        addSubscription(observable, new SubscriberCallBack(callback));
    }

    public void addSubscription(Observable<?> observable, SubscriberCallBack subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeDisposable();
        }
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(subscriber));
    }
}
