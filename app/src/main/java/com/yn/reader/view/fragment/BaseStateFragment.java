package com.yn.reader.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.yn.reader.mvp.presenters.BasePresenter;
import com.yn.reader.util.LogUtils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * fragment基类保存临时数据
 * Created by sunxiangyao on 15/2/11.
 */
public abstract class BaseStateFragment extends BaseFragment {
    public abstract BasePresenter getPresenter();

    /**
     * 网络或者数据库加载数据
     */
    public abstract void loadData();

    /**
     * 保存临时数据(非持久化,退出即消失)
     */
    public abstract void saveStateToArguments(Bundle outState);

    /**
     * 恢复临时数据(非持久化,退出即消失)
     */
    public abstract void restoreData(Bundle savedInstanceState);


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!restoreStateFromArguments(savedInstanceState)) {
            loadData();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtils.log("onSaveInstanceState");
        saveStateToArguments(outState);

    }

    private boolean restoreStateFromArguments(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            restoreData(savedInstanceState);
            return true;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        onUnsubscribe();
        if (getPresenter() != null) {
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
}
