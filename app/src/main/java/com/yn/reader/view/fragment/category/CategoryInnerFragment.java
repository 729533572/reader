package com.yn.reader.view.fragment.category;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yn.reader.R;
import com.yn.reader.mvp.presenters.BasePresenter;
import com.yn.reader.mvp.presenters.CategoryPresenter;
import com.yn.reader.mvp.views.CategoryView;
import com.yn.reader.model.category.CategoryGroup;
import com.yn.reader.model.category.CategoryInner;
import com.yn.reader.util.ComUtils;
import com.yn.reader.util.Constant;
import com.yn.reader.util.SwipeRefreshLayoutUtil;
import com.yn.reader.view.adapter.CategoryInnerAdapter;
import com.yn.reader.view.fragment.BaseFragment;
import com.yn.reader.widget.PostItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 分类
 * Created by sunxy on 2018/2/7.
 */

public class CategoryInnerFragment extends BaseFragment implements CategoryView, SwipeRefreshLayout.OnRefreshListener {
    private CategoryPresenter categoryPresenter;

    private static final String KEY_TYPE = "key_type";

    private final int SPAN_COUNT = 3;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.category_rec)
    RecyclerView recyclerView;

    @BindView(R.id.iv_advertisement_bottom)
    ImageView iv_advertisement_bottom;

    private List<CategoryInner> categoryList;
    private CategoryInnerAdapter mCategoryInnerAdapter;

    @Override
    public void onCategoryLoaded(CategoryGroup categoryGroup) {
        swipeRefreshLayout.setRefreshing(false);
        if (categoryGroup.getData() != null) {
            categoryList.clear();
            for (CategoryInner bean : categoryGroup.getData()) {
                bean.setSex(getArguments().getInt(KEY_TYPE));
                categoryList.add(bean);
            }
            mCategoryInnerAdapter.notifyDataSetChanged();
        }
    }


    public static CategoryInnerFragment getInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, type);
        CategoryInnerFragment fragment = new CategoryInnerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inner_category, container, false);
        ButterKnife.bind(this, view);
        SwipeRefreshLayoutUtil.styleSwipeRefreshLayout(swipeRefreshLayout);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categoryPresenter = new CategoryPresenter(this);

        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
        recyclerView.setLayoutManager(layoutManager);

        categoryList = new ArrayList<>();
        mCategoryInnerAdapter = new CategoryInnerAdapter(getActivity(), categoryList);
        recyclerView.setAdapter(mCategoryInnerAdapter);

        GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        };
        layoutManager.setSpanSizeLookup(spanSizeLookup);

        recyclerView.addItemDecoration(new PostItemDecoration(ComUtils.dip2px(17), spanSizeLookup));
        final Handler handler = new Handler();
        swipeRefreshLayout.setOnRefreshListener(this);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                loadData();
                handler.removeCallbacks(this);
            }
        }, Constant.TIME_DELAY);
    }

    private void loadData() {
        categoryPresenter.load(getArguments().getInt(KEY_TYPE));
    }

    @Override
    public BasePresenter getPresenter() {
        return categoryPresenter;
    }


    @Override
    public Activity getContext() {
        return getActivity();
    }

    @Override
    public void onRefresh() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
                handler.removeCallbacks(this);
            }
        }, Constant.TIME_DELAY);
    }
}
