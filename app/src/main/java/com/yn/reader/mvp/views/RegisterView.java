package com.yn.reader.mvp.views;


import com.yn.reader.model.BaseData;

/**
 * 注册
 * Created by sunxy on 2018/3/13.
 */

public interface RegisterView extends BaseView {
    void onSmsCodeSuccess(BaseData baseData);
    void findBackPasswordSuccess();
    void registerSuccess();
}
