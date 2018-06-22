package com.yn.reader.mvp.presenters;

import android.app.Activity;

import com.yn.reader.mvp.views.BaseView;
import com.yn.reader.mvp.views.RegisterView;
import com.yn.reader.network.api.MiniRest;
import com.yn.reader.model.BaseData;
import com.yn.reader.model.password.PasswordBack;
import com.yn.reader.model.register.RegisterResult;

/**
 * 注册
 * Created by sunxy on 2018/3/13.
 */

public class RegisterPresenter extends BasePresenter {
    private RegisterView registerView;

    public RegisterPresenter(RegisterView registerView) {
        this.registerView = registerView;
    }

    @Override
    public BaseView getBaseView() {
        return registerView;
    }

    @Override
    public void success(Object o) {
        if (o instanceof PasswordBack) {
            registerView.findBackPasswordSuccess();
        } else if (o instanceof RegisterResult) {
            registerView.registerSuccess();
        } else if (o instanceof BaseData) {
            registerView.onSmsCodeSuccess((BaseData) o);
        }
    }

    @Override
    public void detachView() {
        unSubscribe();
    }

    public void getSmsCode(Activity activity, String phone, int type) {
        addSubscription(MiniRest.getInstance().getSmsCode(activity, phone, type));

    }

    public void register(Activity activity, String phone, String verifycode, String password, String repassword) {
        addSubscription(MiniRest.getInstance().register(activity, phone, verifycode, password, repassword));
    }

    public void forgetPassword(Activity activity, String phone, String verifycode, String password, String repassword) {
        addSubscription(MiniRest.getInstance().forgetPassword(activity, phone, verifycode, password, repassword));
    }
}
