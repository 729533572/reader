package com.yn.reader.event;

import com.yn.reader.model.dao.UserInfo;

/**
 * 登录成功事件
 * Created by sunxy on 2018/3/26.
 */

public class LoginSuccessEvent {
    private UserInfo userInfo;

    public LoginSuccessEvent(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
}
