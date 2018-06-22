package com.yn.reader.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.yn.reader.BuildConfig;
import com.yn.reader.R;
import com.yn.reader.base.BaseActivity;
import com.yn.reader.db.UserInfoDao;
import com.yn.reader.event.QuitEvent;
import com.yn.reader.mvp.presenters.SettingPresenter;
import com.yn.reader.mvp.views.SettingView;
import com.yn.reader.network.api.MiniRest;
import com.yn.reader.model.common.BookDBManager;
import com.yn.reader.model.dao.DbHelper;
import com.yn.reader.model.dao.UserInfo;
import com.yn.reader.service.SynchronizeCollectionDataService;
import com.yn.reader.util.BusProvider;
import com.yn.reader.util.ImageCacheUtil;
import com.yn.reader.util.IntentUtils;
import com.yn.reader.util.LogUtils;
import com.yn.reader.util.UserInfoManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity implements SettingView {
    private SettingPresenter mSettingPresenter;

    @BindView(R.id.tv_cache_size)
    TextView tv_cache_size;

    @BindView(R.id.ll_auto_buy)
    LinearLayout ll_auto_buy;

    @BindView(R.id.log_out)
    LinearLayout log_out;

    @BindView(R.id.tv_version_name)
    TextView tv_version_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        setToolbarTitle(R.string.setting_title);
        tv_version_name.setText(BuildConfig.VERSION_NAME);
        UserInfoDao userInfoDao = DbHelper.getInstance().getDaoSession().getUserInfoDao();
        UserInfo userInfo = null;
        try {
            userInfo = userInfoDao.loadAll().get(0);
        } catch (Exception ignored) {
        }

        if (userInfo != null) ll_auto_buy.setSelected(userInfo.getChapterautobuy() == 1);
        mSettingPresenter = new SettingPresenter(this);

        ImageCacheUtil.getInstance().getDiskCacheSize(this, tv_cache_size);

        ll_auto_buy.setVisibility(UserInfoManager.getInstance().isLanded() ? View.VISIBLE : View.GONE);

        log_out.setVisibility(UserInfoManager.getInstance().isLanded() ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.fl_clear_cache)
    public void clearCache() {
        new MaterialDialog.Builder(this)
                .content(R.string.tip_clear_cache)
                .canceledOnTouchOutside(false)
                .negativeText(R.string.cancel)
                .positiveText(R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        ImageCacheUtil.getInstance().clearImageAllCache();
                        tv_cache_size.setText("");
                        ImageCacheUtil.getInstance().getDiskCacheSize(SettingActivity.this, tv_cache_size);
                    }
                }).show();

    }

    @OnClick(R.id.ll_auto_buy)
    public void autoBuy() {
        ll_auto_buy.setSelected(!ll_auto_buy.isSelected());
        mSettingPresenter.setAutoBuy(ll_auto_buy.isSelected());
    }

    @OnClick(R.id.fl_private_agreement)
    public void privateAgreement() {
        IntentUtils.startActivity(this, PrivateAgreementActivity.class, MiniRest.PRIVATE_AGREEMENT,getString(R.string.privacy_item));
    }

    @OnClick(R.id.log_out)
    public void logOut() {
        LogUtils.log("logOut");
        new MaterialDialog.Builder(this)
                .content(R.string.quit_account)
                .canceledOnTouchOutside(false)
                .negativeText(R.string.cancel)
                .positiveText(R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        quit();
                    }
                }).show();
    }

    private void quit() {
        Intent intent = new Intent(this, SynchronizeCollectionDataService.class);
        stopService(intent);

        BookDBManager.getInstance().clear();
        DbHelper.getInstance().getDaoSession().getUserInfoDao().deleteAll();

        BusProvider.getInstance().post(new QuitEvent(true));
        IntentUtils.popPreviousActivity(this);
    }

    @OnClick(R.id.fl_check_newest_version)
    public void checkNewestVersion() {
        mSettingPresenter.checkUpdateVersion();
    }

    @Override
    public Activity getContext() {
        return this;
    }

    @Override
    public void onUpdateUserInfo(UserInfo userInfo) {
        DbHelper.getInstance().getDaoSession().getUserInfoDao().update(userInfo);
        ll_auto_buy.setSelected(userInfo.getChapterautobuy() == 1);
    }
}
