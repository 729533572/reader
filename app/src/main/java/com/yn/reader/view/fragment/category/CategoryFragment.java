package com.yn.reader.view.fragment.category;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yn.reader.R;
import com.yn.reader.mvp.presenters.BasePresenter;
import com.yn.reader.util.IntentUtils;
import com.yn.reader.view.SearchActivity;
import com.yn.reader.view.adapter.CategoryPagerAdapter;
import com.yn.reader.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 分类
 * Created by sunxy on 2018/2/7.
 */

public class CategoryFragment extends BaseFragment {
    @BindView(R.id.category_pager)
    ViewPager mViewPager;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CategoryPagerAdapter categoryPagerAdapter = new CategoryPagerAdapter(getFragmentManager(), getActivity());
        mViewPager.setAdapter(categoryPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @OnClick(R.id.tv_search)
    public void search() {
        IntentUtils.startActivity(getActivity(), SearchActivity.class);
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }


}
