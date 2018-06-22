package com.yn.reader.view.adapter;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.yn.reader.R;
import com.yn.reader.view.fragment.home.BoysFragment;
import com.yn.reader.view.fragment.home.GirlsFragment;
import com.yn.reader.view.fragment.home.RecommendFragment;

/**
 * 主界面（首页-书架-分类-账户）
 * Created by sunxy on 2018/2/7.
 */

public class HomePagerAdapter extends FragmentStatePagerAdapter {
    public static final int PAGER_RECOMMEND = 0, PAGER_BOYS = 1, PAGER_GIRLS = 2;
    private final int PAGER_COUNT = 3;
    private String titles[];
    public HomePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        titles=context.getResources().getStringArray(R.array.home_page_titles);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case PAGER_RECOMMEND:
                return new RecommendFragment();
            case PAGER_BOYS:
                return new BoysFragment();
        }
        return new GirlsFragment();
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return titles[position];
    }
}
