package com.yn.reader.base;

import com.yn.reader.mvp.presenters.BasePresenter;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * Created by sunxy on 2018/1/4.
 */

public abstract class BaseMvpActivity extends BaseActivity {
    public abstract BasePresenter getPresenter();
    @Override
    protected void onDestroy() {
        super.onDestroy();
        onUnsubscribe();
        if(getPresenter()!=null){
            getPresenter().detachView();
        }
    }



    protected CompositeDisposable mCompositeSubscription;

    //RXjava取消注册，以避免内存泄露
    private void onUnsubscribe() {
        if (mCompositeSubscription != null && !mCompositeSubscription.isDisposed()) {
            mCompositeSubscription.dispose();
        }
    }

    protected void addSubscription(Disposable subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeDisposable();
        }
        mCompositeSubscription.add(subscription);
    }
}
