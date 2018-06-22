package com.yn.reader.mvp.views;


import com.yn.reader.model.adconfig.AdConfig;
import com.yn.reader.model.aso.AsoAdResponse;

import java.util.List;

/**
 * 开屏
 * Created by sunxy on 2018/1/10.
 */

public interface SplashView extends BaseView {
    void onAdConfigLoaded(List<AdConfig> adConfigs);

    void onAsoSplashLoaded(AsoAdResponse asoAd);

    void onAsoSplashLoadFailed();
}
