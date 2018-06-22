package com.yn.reader.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.socks.library.KLog;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.yn.reader.MiniApp;
import com.yn.reader.R;
import com.yn.reader.base.BaseMvpActivity;
import com.yn.reader.event.LoginSuccessEvent;
import com.yn.reader.mvp.presenters.BasePresenter;
import com.yn.reader.mvp.presenters.LoginPresenter;
import com.yn.reader.mvp.views.LoginView;
import com.yn.reader.network.api.MiniRest;
import com.yn.reader.model.LoginResult;
import com.yn.reader.model.WeChatInfo;
import com.yn.reader.model.WeChatToken;
import com.yn.reader.model.dao.DbHelper;
import com.yn.reader.model.dao.UserInfo;
import com.yn.reader.util.AppPreference;
import com.yn.reader.util.BusProvider;
import com.yn.reader.util.Constant;
import com.yn.reader.util.DeviceUtil;


import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created : lts .
 * Date: 2017/12/27
 * Email: lts@aso360.com
 * 此类为微信登录activity,严禁修改包名与类名;
 */

public class WXEntryActivity extends BaseMvpActivity implements IWXAPIEventHandler, LoginView {


    @BindView(R.id.userIcon)
    CircleImageView mUserIcon;
    @BindView(R.id.loading)
    ImageView mLoading;
    private IWXAPI mApi;
    private LoginPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx_enter);
        ButterKnife.bind(this);
        initView();

        MiniApp app = (MiniApp) getApplication();
        mApi = app.getIWXAPI();

        mPresenter = new LoginPresenter(this);

    }

    private void initView() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_round_rotate);
        LinearInterpolator interpolator = new LinearInterpolator();
        animation.setInterpolator(interpolator);

        mLoading.startAnimation(animation);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getIntent() != null) {
            mApi.handleIntent(getIntent(), this);
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        mApi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        KLog.d(baseReq.openId);
    }

    @Override
    public void onResp(BaseResp baseResp) {

        if (baseResp.getType() == 2)
            finish();


        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK://用户授权成功
                getToken(baseResp);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL://授权取消
                showActionMessage(R.string.errcode_cancel);
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED://拒绝授权
                showActionMessage(R.string.errcode_deny);
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT://不支持的错误
                showActionMessage(R.string.errcode_unsupported);
                break;
            default:
                showActionMessage(R.string.errcode_unknown);
                break;
        }
    }

    /**
     * 获取token
     *
     * @param baseResp baseResp
     */
    private void getToken(BaseResp baseResp) {
        SendAuth.Resp resp = (SendAuth.Resp) baseResp;

        mPresenter.getWechatToken(resp.code, resp);

    }

    private void showActionMessage(int result) {
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public Activity getContext() {
        return this;
    }

    /**
     * md5(openid +md5(openid + unionid + package + version + idfa + time))
     *
     * @param weChatToken
     */
    @Override
    public void getWeChatSuccess(WeChatToken weChatToken) {

        mPresenter.getWeChatInfo(weChatToken);

    }

    @Override
    public void loginSuccess(LoginResult loginResult) {

        Glide.with(this).load(MiniRest.MINI_HOST_END_POINT + loginResult.getData().getAvatar()).into(mUserIcon);


        if (loginResult.getStatus() == 1) {
            UserInfo userInfo = loginResult.getData();
            DbHelper.getInstance().getDaoSession().getUserInfoDao().insert(userInfo);
            BusProvider.getInstance().post(new LoginSuccessEvent(userInfo));
//            AppPreference.getInstance().setUid(userInfo.getUserid());
//            AppPreference.getInstance().setKey(userInfo.getKey());
        } else {
            Toast.makeText(this, getResources().getString(R.string.login_service_error), Toast.LENGTH_LONG).show();

        }
        finish();
    }



    @Override
    public void getWeChatInfoSuccess(WeChatInfo weChatInfo) {
        //将微信返回的 openid 跟 unionid存储到本地

        AppPreference.getInstance().setString(Constant.OPEN_ID, weChatInfo.getOpenid());
        AppPreference.getInstance().setString(Constant.UNION_ID, weChatInfo.getUnionid());
        mPresenter.login(weChatInfo, DeviceUtil.getIMEI(this), String.valueOf(System.currentTimeMillis() / 1000),1);
        AppPreference.getInstance().setString(Constant.DEVICE_ID, DeviceUtil.getIMEI(this));
    }

    @Override
    public void phoneLoginSuccess(UserInfo userInfo) {

    }


    @Override
    public BasePresenter getPresenter() {
        return mPresenter;
    }
}
