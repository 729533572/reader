package com.yn.reader.mvp.presenters;

import com.hysoso.www.utillibrary.LogUtil;
import com.yn.reader.mvp.views.BaseView;
import com.yn.reader.mvp.views.SplashView;
import com.yn.reader.network.api.Callback;
import com.yn.reader.network.api.MiniRest;
import com.yn.reader.network.api.SubscriberCallBack;
import com.yn.reader.model.adconfig.AdConfigResponse;
import com.yn.reader.model.aso.AsoAdResponse;
import com.yn.reader.util.AppPreference;
import com.yn.reader.util.Constant;

/**
 * 广告信息接口
 * Created by sunxy on 2018/1/10.
 */

public class SplashPresenter extends BasePresenter {
    public static final int ASO_AD_CLICK = 1, ASO_AD_REQUEST_RESULT = 2, ASO_AD_REQUEST = 3, AD_CLICK = 4, AD_REQUEST_RESULT = 5, AD_REQUEST = 6;

    public static final int REQUEST_SUCCESS = 1, REQUEST_FAIL = 2, PRESENT_SUCCESS = 3, PRESENT_FAIL = 4;
    private SplashView splashView;

    public SplashPresenter(SplashView splashView) {
        this.splashView = splashView;
    }

    @Override
    public BaseView getBaseView() {
        return splashView;
    }

    @Override
    public void success(Object o) {
        if (o instanceof AdConfigResponse) {
            LogUtil.i(getClass().getSimpleName(), "AdConfigResponse:" + o);
            AdConfigResponse response = (AdConfigResponse) o;
            if (response.getData() != null) {
                splashView.onAdConfigLoaded(response.getData());
            }
        }else if (o instanceof AsoAdResponse) {
            AsoAdResponse response = (AsoAdResponse) o;
            splashView.onAsoSplashLoaded(response);
        }

    }

    @Override
    public void onFailure(int code, String msg) {
        super.onFailure(code, msg);
//        splashView.error(code,msg);
    }

    @Override
    public void detachView() {
        unSubscribe();
    }

    public void loadAdConfig() {
        addSubscription(MiniRest.getInstance().getAdConfig());
    }

    public void getAsoSplash(int positionId) {
        addSubscription(MiniRest.getInstance().getAsoSplash(positionId), new SubscriberCallBack(new Callback() {
            @Override
            public void onSuccess(Object o) {
                if (o instanceof AsoAdResponse) {
                    AsoAdResponse response = (AsoAdResponse) o;
                    if (response.getStatus() == 1) {
                        splashView.onAsoSplashLoaded(response);
                    } else {
                        splashView.onAsoSplashLoadFailed();
                    }
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                splashView.onAsoSplashLoadFailed();
            }

            @Override
            public void onCompleted() {

            }
        }));
    }


    public void adRecord(int recordType, int type, String adId, int positionId, int adProvider) {
        switch (recordType) {
            case ASO_AD_CLICK:
                advertisementClickStatistics(adProvider, positionId, type, adId);
                break;
            case ASO_AD_REQUEST_RESULT:
                advertisementRequestStatistics(type, adProvider, positionId, Constant.AdNativeID, adId);
                break;

            case AD_CLICK:
                advertisementClickStatistics(adProvider, positionId, type, adId);
                break;
            case AD_REQUEST:
                advertisementRequestStatistics(recordType, adProvider, positionId, type, adId);
                break;
        }
    }


    /**
     * 广告集成平台专用
     *
     * @param type
     * @param adType
     * @param positionId
     * @param adProvider
     */
    public void adResultRecord(int type, int adType, int positionId, int adProvider) {
        advertisementRequestStatistics(type, adProvider, positionId, adType, AppPreference.getInstance().getAdId());
    }

    public void advertisementClickStatistics(
            int adprovider,
            int positionid,
            int adtype,
            String adid) {
        addSubscription(MiniRest.getInstance().advertisementClickStatistics(adprovider, positionid, adtype, adid));
    }

    public void advertisementRequestStatistics(
            int result_type,
            int adprovider,
            int positionid,
            int adtype,
            String adid) {
        addSubscription(MiniRest.getInstance().advertisementRequestStatistics(result_type, adprovider, positionid, adtype, adid));
    }

    public void advertisementSkipStatistics(
            int adprovider,
            int positionid,
            String adid) {
        addSubscription(MiniRest.getInstance().advertisementSkipStatistics(adprovider, positionid, adid));
    }


    public void newlyAddedStatistics() {
        addSubscription(MiniRest.getInstance().newlyAddedStatistics());
    }


}
