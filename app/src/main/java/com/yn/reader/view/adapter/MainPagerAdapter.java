package com.yn.reader.view.adapter;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.yn.reader.view.fragment.shelf.BookShelfFragment;
import com.yn.reader.view.fragment.category.CategoryFragment;
import com.yn.reader.view.fragment.home.HomeFragment;
import com.yn.reader.view.fragment.ProfileFragment;

/**
 * 主界面（首页-书架-分类-账户）
 * Created by sunxy on 2018/2/7.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {
    public static final int PAGER_HOME = 0, PAGER_SHELF = 1, PAGER_CATEGORY = 2, PAGER_PROFILE = 3;
    private final int PAGER_COUNT = 4;

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case PAGER_HOME:
                return new HomeFragment();
            case PAGER_SHELF:
                return new BookShelfFragment();
            case PAGER_CATEGORY:
                return new CategoryFragment();
        }
        return new ProfileFragment();
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }
}
