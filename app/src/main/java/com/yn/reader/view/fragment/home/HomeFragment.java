package com.yn.reader.view.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yn.reader.R;
import com.yn.reader.mvp.presenters.BasePresenter;
import com.yn.reader.util.ComUtils;
import com.yn.reader.view.MainActivity;
import com.yn.reader.view.SearchActivity;
import com.yn.reader.view.adapter.HomePagerAdapter;
import com.yn.reader.view.fragment.BaseFragment;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首页
 * Created by sunxy on 2018/2/7.
 */

public class HomeFragment extends BaseFragment {
    @BindView(R.id.home_search_bar)
    View mTopSearchBar;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.home_pager)
    ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reflex(mTabLayout);
        int screen_width = getResources().getDisplayMetrics().widthPixels - ComUtils.dip2px(32);
        int tab_layout_width = (int) (screen_width * 5.0 / 9);
        mTabLayout.getLayoutParams().width = tab_layout_width;
        mTopSearchBar.getLayoutParams().width = screen_width - tab_layout_width;

        mViewPager.setOffscreenPageLimit(4);
        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getFragmentManager(), getActivity());
        mViewPager.setAdapter(homePagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @OnClick(R.id.home_search_bar)
    public void search() {
        startActivity(new Intent(getActivity(), SearchActivity.class));
    }

    public void reflex(final TabLayout tabLayout) {
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);

                    int dp6 = ComUtils.dip2px(6);

                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = ComUtils.dip2px(12);
                        params.rightMargin = ComUtils.dip2px(12);
                        tabView.setLayoutParams(params);

                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }


}
