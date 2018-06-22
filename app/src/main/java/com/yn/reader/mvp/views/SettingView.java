package com.yn.reader.mvp.views;

import com.yn.reader.model.dao.UserInfo;

/**
 * Created by luhe on 2018/3/28.
 */

public interface SettingView extends BaseView{
    void onUpdateUserInfo(UserInfo userInfo);
}
