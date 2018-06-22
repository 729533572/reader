package com.yn.reader.view.adapter;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.yn.reader.R;
import com.yn.reader.view.fragment.BaseFragment;
import com.yn.reader.view.fragment.notice.NoticeFragment;

/**
 * 消息中心（系统消息用户消息）
 * Created by sunxy on 2018/2/7.
 */

public class NoticePagerAdapter extends FragmentStatePagerAdapter {
    public static final int PAGER_NOTICE_SYSTEM = 1,PAGER_NOTICE_USER = 2;
    private final int PAGER_COUNT = 2;
    private String titles[];
    private BaseFragment[] mBaseFragments;

    public NoticePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        titles = context.getResources().getStringArray(R.array.notice_page_titles);
        mBaseFragments = new BaseFragment[titles.length];
    }

    @Override
    public Fragment getItem(int position) {
        BaseFragment fragment = null;
        if(position==0)fragment =  NoticeFragment.getInstance(PAGER_NOTICE_SYSTEM);
        else fragment =  NoticeFragment.getInstance(PAGER_NOTICE_USER);
        mBaseFragments[position] = fragment;
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    public BaseFragment[] getBaseFragments() {
        return mBaseFragments;
    }
}
