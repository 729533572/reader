package com.yn.reader.mvp.views;


import com.yn.reader.model.LoginResult;

/**
 * Created : lts .
 * Date: 2018/1/3
 * Email: lts@aso360.com
 */

public interface EditUserProfileView extends BaseView {

    void upDateUserProfile(LoginResult loginResult);

    void error();
}
