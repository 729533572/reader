package com.yn.reader.mvp.views;


import com.yn.reader.model.LoginResult;
import com.yn.reader.model.WeChatInfo;
import com.yn.reader.model.WeChatToken;
import com.yn.reader.model.dao.UserInfo;

/**
 * Created : lts .
 * Date: 2017/12/28
 * Email: lts@aso360.com
 */

public interface LoginView extends BaseView {

    void getWeChatSuccess(WeChatToken weChatToken);

    void loginSuccess(LoginResult loginResult);

    void getWeChatInfoSuccess(WeChatInfo weChatInfo);

    void phoneLoginSuccess(UserInfo userInfo);
}
