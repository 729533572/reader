package com.yn.reader.base;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.umeng.analytics.game.UMGameAgent;
import com.yn.reader.R;
import com.yn.reader.statusbar.ImmersiveStatusBarCompat;
import com.yn.reader.util.ToolbarUtils;

import butterknife.BindView;

/**
 * 基类
 * Created by sunxy on 2017/12/27.
 */

public class BaseActivity extends RxAppCompatActivity {

    @Nullable
    @BindView(R.id.base_toolbar)
    protected Toolbar mToolbar;
    @Nullable
    @BindView(R.id.toolbar_shadow)
    public View mToolbarShadow;
    @Nullable
    @BindView(R.id.tv_title)
    protected TextView mTitle;
    @Nullable
    @BindView(R.id.toolbar_back)
    protected ImageView mTitleBack;
    @Nullable
    @BindView(R.id.save)
    protected TextView mSave;


    @Override
    protected void onPause() {
        super.onPause();
        UMGameAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UMGameAgent.onResume(this);
    }

    protected void hideToolbarShadow() {
        mToolbarShadow.setVisibility(View.GONE);
    }

    protected void setToolbarTitle(@StringRes int title) {
        if (mToolbar != null) {
            ToolbarUtils.styleToolBar(this, mToolbar, mTitleBack, mTitle, getString(title));
        }
    }
    protected void setToolbarTitle(@StringRes int title, View.OnClickListener finishListener) {
        if (mToolbar != null) {
            ToolbarUtils.styleToolBar(this, mToolbar, mTitleBack, mTitle, getString(title));
        }
    }
    protected void setToolbarTitle(String title) {
        if (mToolbar != null) {
            ToolbarUtils.styleToolBar(this, mToolbar, mTitleBack, mTitle, title);
        }
    }

    protected void setStatusBarColor2Green() {
        ImmersiveStatusBarCompat.setStatusBarColor(getWindow(),false,getResources().getColor(R.color.colorPrimary),false);
    }

    protected void setStatusBarColor2White() {
        ImmersiveStatusBarCompat.setStatusBarColor(getWindow(),false,getResources().getColor(R.color.white),true);
    }

    protected void setStatusBarColor2Black() {
        ImmersiveStatusBarCompat.setStatusBarColor(getWindow(),false,getResources().getColor(R.color.black),false);
    }


}
