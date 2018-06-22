package com.yn.reader.view.adapter;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.yn.reader.R;
import com.yn.reader.view.fragment.BaseFragment;
import com.yn.reader.view.fragment.shelf.CollectionFragment;
import com.yn.reader.view.fragment.shelf.HistoryFragment;
import com.yn.reader.view.fragment.shelf.ShelfItemFragment;

/**
 * 主界面（首页-书架-分类-账户）
 * Created by sunxy on 2018/2/7.
 */

public class BookShelfPagerAdapter extends FragmentStatePagerAdapter {
    public static final int PAGER_FAVORITE = 0, PAGER_HISTORY = 1;
    private final int PAGER_COUNT = 2;
    private String titles[];
    private ShelfItemFragment[] mFragments;

    public BookShelfPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        titles = context.getResources().getStringArray(R.array.shelf_page_titles);
        mFragments = new ShelfItemFragment[titles.length];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case PAGER_FAVORITE:
                mFragments[position] = CollectionFragment.getInstance();
                break;
            case PAGER_HISTORY:
                mFragments[position] = HistoryFragment.getInstance();
                break;
        }
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    public BaseFragment[] getFragments() {
        return mFragments;
    }
}
