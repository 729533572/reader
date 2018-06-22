package com.yn.reader.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hysoso.www.utillibrary.BroadCastUtil;
import com.hysoso.www.utillibrary.LogUtil;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.yn.reader.R;
import com.yn.reader.base.BaseActivity;
import com.yn.reader.service.SynchronizeCollectionDataService;
import com.yn.reader.view.adapter.MainPagerAdapter;
import com.yn.reader.view.fragment.shelf.BookShelfFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements OnTabSelectListener, OnTabReselectListener, ViewPager.OnPageChangeListener {
    @BindView(R.id.bottomBar)
    BottomBar bottomBar;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tv_bottom_left)
    TextView selectAll;

    @BindView(R.id.delete)
    TextView delete;

    @BindView(R.id.bottom_divider)
    View bottom_divider;

    @BindView(R.id.operation_layout)
    View operation_layout;

    private boolean isEditModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusBarColor2White();
        ButterKnife.bind(this);
        mViewPager.setAdapter(new MainPagerAdapter(getFragmentManager()));

        bottomBar.setOnTabSelectListener(this);
        bottomBar.setOnTabReselectListener(this);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(4);

        selectAll.setText(R.string.select_all);

        Intent intent = new Intent(this, SynchronizeCollectionDataService.class);
        startService(intent);
    }


    @Override
    public void onTabReSelected(int tabId) {
        switch (tabId) {
            case R.id.tab_home:
                break;
            case R.id.tab_bookshelf:
                break;
            case R.id.tab_category:
                break;
            case R.id.tab_account:
                break;
        }
    }

    @Override
    public void onTabSelected(int tabId) {
        switch (tabId) {
            case R.id.tab_home:
                mViewPager.setCurrentItem(MainPagerAdapter.PAGER_HOME);
                setStatusBarColor2White();
                break;
            case R.id.tab_bookshelf: {
                LogUtil.i(getClass().getSimpleName(), "tab_bookshelf");
                mViewPager.setCurrentItem(MainPagerAdapter.PAGER_SHELF);
                setStatusBarColor2Green();
            }
            break;
            case R.id.tab_category:
                mViewPager.setCurrentItem(MainPagerAdapter.PAGER_CATEGORY);
                setStatusBarColor2Green();
                break;
            case R.id.tab_account:
                mViewPager.setCurrentItem(MainPagerAdapter.PAGER_PROFILE);
                setStatusBarColor2Green();
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        bottomBar.selectTabAtPosition(position);
        if (position == MainPagerAdapter.PAGER_SHELF)
            BroadCastUtil.sendLocalBroadcast(this,
                    BookShelfFragment.ShelfEditBroadCastReceiver.class,
                    BookShelfFragment.ShelfEditBroadCastReceiver.REFRESH);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void setEditInstallStatus() {
        isEditModel = false;
        setEditModelStatus();
    }

    public void setEditModelStatus() {
        if (isEditModel) {
            operation_layout.setVisibility(View.VISIBLE);
            selectAll.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
            bottom_divider.setVisibility(View.VISIBLE);
            bottomBar.setVisibility(View.GONE);
            BroadCastUtil.sendLocalBroadcast(this,
                    BookShelfFragment.ShelfEditBroadCastReceiver.class,
                    BookShelfFragment.ShelfEditBroadCastReceiver.ON_EDIT);
        } else {
            delete.setVisibility(View.GONE);
            bottom_divider.setVisibility(View.GONE);
            operation_layout.setVisibility(View.GONE);
            bottomBar.setVisibility(View.VISIBLE);

            BroadCastUtil.sendLocalBroadcast(this,
                    BookShelfFragment.ShelfEditBroadCastReceiver.class,
                    BookShelfFragment.ShelfEditBroadCastReceiver.ON_COMPLETE);
        }
    }

    @OnClick(R.id.tv_bottom_left)
    public void selectAll() {

        BroadCastUtil.sendLocalBroadcast(this,
                BookShelfFragment.ShelfEditBroadCastReceiver.class,
                BookShelfFragment.ShelfEditBroadCastReceiver.SELECT_ALL);

    }

    public void setBottomLeft(String text) {
        ((TextView) findViewById(R.id.tv_bottom_left)).setText(text);
    }

    @OnClick(R.id.delete)
    public void deleteSelect() {
        BroadCastUtil.sendLocalBroadcast(this,
                BookShelfFragment.ShelfEditBroadCastReceiver.class,
                BookShelfFragment.ShelfEditBroadCastReceiver.DELETE_SELECTION);
    }

    @Override
    public void onBackPressed() {
        if (isEditModel) {
            isEditModel = false;
            setEditModelStatus();
        } else {
            new MaterialDialog.Builder(this)
                    .content(R.string.quit_application)
                    .canceledOnTouchOutside(false)
                    .negativeText(R.string.cancel)
                    .positiveText(R.string.ok)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                            MainActivity.super.onBackPressed();
                        }
                    }).show();
        }
    }

    public boolean isEditModel() {
        return isEditModel;
    }

    public void setEditModel(boolean editModel) {
        isEditModel = editModel;
    }
}
