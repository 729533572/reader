package com.yn.reader.view.fragment.shelf;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hysoso.www.utillibrary.BroadCastUtil;
import com.hysoso.www.utillibrary.LogUtil;
import com.yn.reader.R;
import com.yn.reader.mvp.presenters.BasePresenter;
import com.yn.reader.util.EditListener;
import com.yn.reader.view.MainActivity;
import com.yn.reader.view.adapter.BookShelfPagerAdapter;
import com.yn.reader.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 书架
 * Created by sunxy on 2018/2/7.
 */

public class BookShelfFragment extends BaseFragment {
    @BindView(R.id.shelf_pager)
    ViewPager mViewPager;

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;

    @BindView(R.id.tv_edit)
    TextView tv_edit;

    private BookShelfPagerAdapter bookShelfPagerAdapter;

    private ShelfEditBroadCastReceiver mShelfEditBroadCastReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_shelf, container, false);
        ButterKnife.bind(this, view);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                ((MainActivity) getActivity()).setEditInstallStatus();
                tv_edit.setText(((MainActivity) getActivity()).isEditModel() ? "完成" : "编辑");
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        return view;
    }

    @OnClick(R.id.tv_edit)
    public void onSaveClicked() {
        ShelfItemFragment shelfItemFragment = (ShelfItemFragment) bookShelfPagerAdapter.getFragments()[mViewPager.getCurrentItem()];
        if (shelfItemFragment.getBookList().size() == 0 && !((MainActivity) getActivity()).isEditModel())
            return;
        ((MainActivity) getActivity()).setEditModel(!((MainActivity) getActivity()).isEditModel());
        tv_edit.setText(((MainActivity) getActivity()).isEditModel() ? "完成" : "编辑");
        ((MainActivity) getActivity()).setEditModelStatus();
        ((MainActivity) getActivity()).setBottomLeft("全选");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mShelfEditBroadCastReceiver = new ShelfEditBroadCastReceiver();
        IntentFilter filter = new IntentFilter(ShelfEditBroadCastReceiver.class.getSimpleName());
        LocalBroadcastManager.getInstance(mViewPager.getContext()).registerReceiver(mShelfEditBroadCastReceiver, filter);

        bookShelfPagerAdapter = new BookShelfPagerAdapter(getFragmentManager(), getActivity());
        mViewPager.setAdapter(bookShelfPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(mViewPager.getContext()).unregisterReceiver(mShelfEditBroadCastReceiver);
        super.onDestroy();
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    private EditListener getCurrentEditListener() {
        BaseFragment fragment = bookShelfPagerAdapter.getFragments()[mViewPager.getCurrentItem()];
        return (EditListener) fragment;
    }

    public class ShelfEditBroadCastReceiver extends BroadcastReceiver {
        public static final int ON_COMPLETE = 0;
        public static final int ON_EDIT = 1;
        public static final int SELECT_ALL = 2;
        public static final int DELETE_SELECTION = 3;
        public static final int REFRESH = 4;

        @Override
        public void onReceive(Context context, Intent intent) {
            int options = intent.getIntExtra(ShelfEditBroadCastReceiver.class.getSimpleName(), 0);
            LogUtil.i(getClass().getSimpleName(), "onReceive:" + options);
            switch (options) {
                case ON_COMPLETE:
                    getCurrentEditListener().onCompleteClicked();
                    break;
                case ON_EDIT:
                    if (isAdded() && isVisible())
                        getCurrentEditListener().onEditClicked();
                    break;
                case SELECT_ALL:
                    if (isAdded() && isVisible())
                        getCurrentEditListener().onSelectAllChecked();
                    break;
                case DELETE_SELECTION:
                    if (isAdded() && isVisible())
                        getCurrentEditListener().deleteSelect();
                    break;
                case REFRESH:
                    if (isAdded() && isVisible()) {
                        getCurrentEditListener().refresh();
                    }
                    break;
            }
        }
    }

}
