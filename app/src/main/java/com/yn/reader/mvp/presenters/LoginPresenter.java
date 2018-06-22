package com.yn.reader.mvp.presenters;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.yn.reader.BuildConfig;
import com.yn.reader.mvp.views.BaseView;
import com.yn.reader.mvp.views.LoginView;
import com.yn.reader.network.api.MiniRest;
import com.yn.reader.model.BaseData;
import com.yn.reader.model.LoginResult;
import com.yn.reader.model.WeChatInfo;
import com.yn.reader.model.WeChatToken;
import com.yn.reader.util.ComUtils;
import com.yn.reader.util.Constant;

/**
 * Created : lts .
 * Date: 2017/12/28
 * Email: lts@aso360.com
 */

public class LoginPresenter extends BasePresenter {

    private LoginView mLoginView;


    public LoginPresenter(LoginView loginView) {
        this.mLoginView = loginView;
    }

    public void getWechatToken(String code,SendAuth.Resp resp) {

        addSubscription(MiniRest.getInstance().getWechatToken(code));
    }

    public void getWeChatInfo(WeChatToken token) {
        addSubscription(MiniRest.getInstance().getWeChatInfo(token.getAccess_token(),token.getOpenid()));
    }

    public void phoneLogin(String phone,String password) {
        addSubscription(MiniRest.getInstance().login(phone, password));
    }

    public void login(WeChatInfo weChatToken, String IMEI, String time,int source){
        addSubscription(MiniRest.getInstance().Login(weChatToken.getOpenid(),weChatToken.getUnionid()
                ,IMEI,time,getToken(weChatToken , IMEI,time),weChatToken.getHeadimgurl(),ComUtils.getEncodeString(weChatToken.getNickname())
                ,weChatToken.getSex(),weChatToken.getProvince(),weChatToken.getCity(),weChatToken.getCountry(),source));
    }


    /**
     * md5(openid +md5(openid + unionid + package + version + idfa + time))
     * @return
     */
    private String getToken(WeChatInfo weChatToken, String imei,String time) {
        String encode2hex = ComUtils.encode2hex(weChatToken.getOpenid() + weChatToken.getUnionid() +
                Constant.PACKAGE_NAME + BuildConfig.VERSION_NAME + imei + time);


        return ComUtils.encode2hex(weChatToken.getOpenid() + encode2hex);
    }

    @Override
    public BaseView getBaseView() {
        return mLoginView;
    }

    @Override
    public void success(Object o) {

        if (o instanceof WeChatToken) {
            WeChatToken weChatToken = (WeChatToken) o;
            mLoginView.getWeChatSuccess(weChatToken);
        } else if (o instanceof LoginResult) {
            LoginResult loginResult = (LoginResult) o;
            mLoginView.loginSuccess(loginResult);
        } else if (o instanceof WeChatInfo) {
            WeChatInfo weChatInfo = (WeChatInfo) o;
            mLoginView.getWeChatInfoSuccess(weChatInfo);
        }
    }

    @Override
    public void onFailure(int code, String msg) {
        super.onFailure(code, msg);
    }

    @Override
    public void detachView() {
        unSubscribe();
    }
}
