package com.yn.reader.mvp.views;

import com.yn.reader.model.dao.UserInfo;

/**
 * Created by luhe on 2018/3/29.
 */

public interface ProfileView extends BaseView{
    void onLoadUserInitializedInfo(UserInfo userInfo);
}
