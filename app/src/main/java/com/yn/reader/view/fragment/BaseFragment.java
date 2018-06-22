package com.yn.reader.view.fragment;

import android.app.Fragment;
import android.os.Bundle;


import com.yn.reader.mvp.presenters.BasePresenter;


import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * fragment基类
 * Created by sunxiangyao on 15/2/11.
 */
public abstract class BaseFragment extends Fragment {
    public abstract BasePresenter getPresenter();

    @Override
    public void onDestroyView() {
        onUnsubscribe();
        if(getPresenter()!=null){
            getPresenter().detachView();
        }
        super.onDestroyView();
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }
}
