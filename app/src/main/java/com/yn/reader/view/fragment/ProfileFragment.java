package com.yn.reader.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hysoso.www.utillibrary.LogUtil;
import com.hysoso.www.utillibrary.StringUtil;
import com.hysoso.www.viewlibrary.MFSRoundImageView;
import com.squareup.otto.Subscribe;
import com.yn.reader.R;
import com.yn.reader.event.LoginSuccessEvent;
import com.yn.reader.event.QuitEvent;
import com.yn.reader.mvp.presenters.BasePresenter;
import com.yn.reader.mvp.presenters.ProfilePresenter;
import com.yn.reader.mvp.views.ProfileView;
import com.yn.reader.model.dao.UserInfo;
import com.yn.reader.util.BusProvider;
import com.yn.reader.util.Constant;
import com.yn.reader.util.GlideUtils;
import com.yn.reader.util.IntentUtils;
import com.yn.reader.util.UserInfoManager;
import com.yn.reader.view.BuyActivity;
import com.yn.reader.view.ConsumptionRecordActivity;
import com.yn.reader.view.EditUserProfileActivity;
import com.yn.reader.view.LoginActivity;
import com.yn.reader.view.LoginTipActivity;
import com.yn.reader.view.NoticeActivity;
import com.yn.reader.view.RechargeRecordActivity;
import com.yn.reader.view.SettingActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 个人中心
 * Created by sunxy on 2018/2/7.
 */

public class ProfileFragment extends BaseFragment implements ProfileView {
    private ProfilePresenter mProfilePresenter;
    @BindView(R.id.avatar)
    MFSRoundImageView avatar;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.btn_login)
    TextView btnLogin;
    @BindView(R.id.vip_remain_day)
    TextView vip_remain_day;
    @BindView(R.id.read_point)
    TextView read_point;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.getInstance().register(this);
        mProfilePresenter = new ProfilePresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        TextPaint tp = userName.getPaint();
        tp.setFakeBoldText(true);

        return view;
    }

    @Override
    public void onResume() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (UserInfoManager.getInstance().isLanded()) {
                    userName.setText(UserInfoManager.getInstance().getUser().getNickname());
                    userName.setVisibility(View.VISIBLE);
                    LogUtil.i(getClass().getSimpleName(), "头像：" + UserInfoManager.getInstance().getUser().getAvatar());
                    if (StringUtil.isEmpty(UserInfoManager.getInstance().getUser().getAvatar()))
                        avatar.setImageResource(R.drawable.default_avatar);
                    else
                        GlideUtils.load(getContext(), UserInfoManager.getInstance().getUser().getAvatar(), avatar);
                    btnLogin.setVisibility(View.GONE);
                } else {
                    avatar.setImageResource(R.drawable.default_avatar);
                    btnLogin.setVisibility(View.VISIBLE);
                    userName.setVisibility(View.GONE);
                }
            }
        }, 200);
        super.onResume();
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @OnClick({R.id.avatar, R.id.user_name})
    public void gotoEditUserInfo() {
        if (!UserInfoManager.getInstance().isLanded())
            IntentUtils.startActivity(getContext(), LoginActivity.class);
        else
            IntentUtils.startActivity(getActivity(), EditUserProfileActivity.class);
    }

    @OnClick(R.id.fl_consumption_record)
    public void consumptionRecord() {
        if (UserInfoManager.getInstance().isLanded())
            startActivity(new Intent(getActivity(), ConsumptionRecordActivity.class));
        else IntentUtils.startActivity(getContext(), LoginTipActivity.class);
    }

    @OnClick(R.id.fl_recharge_record)
    public void rechargeRecord() {
        if (UserInfoManager.getInstance().isLanded())
            startActivity(new Intent(getActivity(), RechargeRecordActivity.class));
        else IntentUtils.startActivity(getContext(), LoginTipActivity.class);
    }

    @OnClick(R.id.btn_login)
    public void login() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    @OnClick(R.id.setting_layout)
    public void setting() {
        startActivity(new Intent(getActivity(), SettingActivity.class));
    }

    @OnClick(R.id.notice_layout)
    public void notice() {
        if (UserInfoManager.getInstance().isLanded())
            startActivity(new Intent(getActivity(), NoticeActivity.class));
        else IntentUtils.startActivity(getContext(), LoginTipActivity.class);

    }

    @OnClick(R.id.recharge)
    public void recharge() {
        if (UserInfoManager.getInstance().isLanded())
            IntentUtils.startActivity(getActivity(), BuyActivity.class, Constant.BUY_TYPE_MONTHLY);
        else IntentUtils.startActivity(getContext(), LoginTipActivity.class);
    }

    @OnClick(R.id.tv_buy_reading_point)
    public void buyReadingPoint() {
        if (UserInfoManager.getInstance().isLanded())
            IntentUtils.startActivity(getActivity(), BuyActivity.class, Constant.BUY_TYPE_READ_POINT);
        else IntentUtils.startActivity(getContext(), LoginTipActivity.class);
    }

    @Subscribe
    public void loginSuccess(LoginSuccessEvent event) {
        LogUtil.i(getClass().getSimpleName(), "loginSuccess");
        bindUserInfo(event.getUserInfo());
    }

    @Subscribe
    public void quit(QuitEvent event) {
        LogUtil.i(getClass().getSimpleName(), "quit");
        btnLogin.setVisibility(View.VISIBLE);

        userName.setVisibility(View.GONE);
        userName.setText("");

        GlideUtils.load(getActivity(), R.drawable.default_avatar, avatar);
        vip_remain_day.setText(0 + "");
        read_point.setText(0 + "");
    }

    private void bindUserInfo(UserInfo userInfo) {
        btnLogin.setVisibility(View.GONE);
        userName.setVisibility(View.VISIBLE);
        userName.setText(userInfo.getNickname());

        GlideUtils.load(getActivity(), userInfo.getAvatar(), avatar, R.drawable.default_avatar);

        vip_remain_day.setText(userInfo.getReadvipday());
        read_point.setText(String.valueOf(userInfo.getCoin()));
    }

    @Override
    public Activity getContext() {
        return getActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onLoadUserInitializedInfo(UserInfo userInfo) {
        bindUserInfo(userInfo);
    }
}
