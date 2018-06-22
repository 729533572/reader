package com.yn.reader.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.hysoso.www.utillibrary.LogUtil;
import com.hysoso.www.utillibrary.StringUtil;
import com.hysoso.www.utillibrary.TimeUtil;
import com.yn.reader.network.api.Callback;
import com.yn.reader.network.api.MiniRest;
import com.yn.reader.network.api.SubscriberCallBack;
import com.yn.reader.model.BaseData;
import com.yn.reader.model.common.Book;
import com.yn.reader.model.common.BookDBManager;
import com.yn.reader.model.favorite.FavoritePost;
import com.yn.reader.util.UserInfoManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by luhe on 2018/3/28.
 */

public class SynchronizeCollectionDataService extends Service implements Callback {
    CompositeDisposable mCompositeSubscription;
    private List<Book> mUploadingBooks = new ArrayList<>();
    private Thread mThread;

    @Override
    public void onCreate() {

        super.onCreate();
    }

    private void updateFavorite() {
        if (mThread == null) {
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        List<Book> waitingUploadBooks = BookDBManager.getInstance().getUnUploadedCollections();
                        if (UserInfoManager.getInstance().isLanded() && waitingUploadBooks.size() > 0 && mUploadingBooks.size() == 0) {
                            mUploadingBooks.addAll(waitingUploadBooks);
                            List<FavoritePost> favoritePosts = new ArrayList<>();
                            for (Book bean : mUploadingBooks) {
                                LogUtil.i(getClass().getSimpleName(), "updateFavorite:" + bean.getBookname());
                                FavoritePost favoritePost = new FavoritePost();
                                favoritePost.setUserid(UserInfoManager.getInstance().getUid());
                                favoritePost.setBookid(bean.getBookid());
                                favoritePost.setChapterid(bean.getChapterid());
                                favoritePost.setProgress(StringUtil.isEmpty(bean.getChapterprogress()) ? "0" : bean.getChapterprogress());
                                favoritePost.setUpdatedate(TimeUtil.getCurrentTime() + "");
                                favoritePosts.add(favoritePost);
                            }
                            addSubscription(MiniRest.getInstance().addFavorite(JSON.toJSONString(favoritePosts)));
                        }
                    }
                }
            });
            mThread.start();
        }
    }

    private void addSubscription(Observable<?> observable) {
        addSubscription(observable, new SubscriberCallBack(this));
    }

    private void addSubscription(Observable<?> observable, SubscriberCallBack subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeDisposable();
        }
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(subscriber));
    }

    //RXjava取消注册，以避免内存泄露
    private void unSubscribe() {
        if (mCompositeSubscription != null && !mCompositeSubscription.isDisposed()) {
            mCompositeSubscription.dispose();
        }
    }

    @Override
    public void onSuccess(Object o) {
        synchronized (this) {
            if (o instanceof BaseData) {
                if (((BaseData) o).getStatus() == 1) {
                    for (Book bean : mUploadingBooks) {
                        BookDBManager.getInstance().setBookUploaded(bean.getBookid(), true);
                    }
                }
                mUploadingBooks.clear();
            }
        }
    }

    @Override
    public void onFailure(int code, String msg) {
        mUploadingBooks.clear();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.i(getClass().getSimpleName(), "onStartCommand:" + flags + "==>" + startId);
        updateFavorite();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onDestroy() {
        unSubscribe();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
