package com.yn.reader.view.adapter;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.yn.reader.R;
import com.yn.reader.view.fragment.category.CategoryInnerFragment;

/**
 * 主界面（首页-书架-分类-账户）
 * Created by sunxy on 2018/2/7.
 */

public class CategoryPagerAdapter extends FragmentStatePagerAdapter {
    public static final int PAGER_BOY = 1, PAGER_GIRL = 2;
    private final int PAGER_COUNT = 2;
    private String titles[];
    public CategoryPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        titles=context.getResources().getStringArray(R.array.category_page_titles);
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0) {
            return CategoryInnerFragment.getInstance(PAGER_BOY);
        }
        return CategoryInnerFragment.getInstance(PAGER_GIRL);
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
