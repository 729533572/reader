package com.yn.reader.mvp.presenters;

import com.allenliu.versionchecklib.core.AllenChecker;
import com.allenliu.versionchecklib.core.VersionParams;
import com.yn.reader.BuildConfig;
import com.yn.reader.mvp.views.BaseView;
import com.yn.reader.mvp.views.SettingView;
import com.yn.reader.network.api.MiniRest;
import com.yn.reader.model.LoginResult;
import com.yn.reader.service.CheckVersionService;
import com.yn.reader.util.Constant;
import com.yn.reader.view.CustomVersionDialogActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by luhe on 2018/3/28.
 */

public class SettingPresenter extends BasePresenter {
    private SettingView mSettingView;

    public SettingPresenter(SettingView settingView) {
        mSettingView = settingView;
    }

    @Override
    public BaseView getBaseView() {
        return mSettingView;
    }

    @Override
    public void success(Object o) {
        if (o instanceof LoginResult) {
            mSettingView.onUpdateUserInfo(((LoginResult) o).getData());
        }
    }

    @Override
    public void detachView() {
        unSubscribe();
    }

    public void setAutoBuy(boolean isAuto) {
        addSubscription(MiniRest.getInstance().upDataUserInfo("chapterautobuy", String.valueOf(isAuto ? 1 : 0)));
    }

    /**
     * 检查版本更新
     */
    public void checkUpdateVersion() {
        Observable<Integer> observable = Observable.just(1).delay(2 * 1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread());
        addSubscription(observable);
        observable.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Integer value) {
                String loadUrl =
                        MiniRest.MINI_HOST_END_POINT_UPDATE_VERSION
                                + "ddz/systemconfig?version="
                                + BuildConfig.VERSION_NAME
                                + "&package="
                                + Constant.PACKAGE_NAME;

                VersionParams.Builder builder = new VersionParams.Builder()
                        .setRequestUrl(loadUrl)
                        .setCustomDownloadActivityClass(CustomVersionDialogActivity.class)
                        .setShowNotification(true)
                        .setService(CheckVersionService.class);

                AllenChecker.startVersionCheck(mSettingView.getContext().getApplication(), builder.build());
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }
}
