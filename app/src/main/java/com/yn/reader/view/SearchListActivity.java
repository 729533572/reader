package com.yn.reader.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hysoso.www.utillibrary.StringUtil;
import com.hysoso.www.utillibrary.ToastUtil;
import com.yn.reader.R;
import com.yn.reader.base.BaseMvpActivity;
import com.yn.reader.mvp.presenters.BasePresenter;
import com.yn.reader.mvp.presenters.SearchListPresenter;
import com.yn.reader.mvp.views.SearchListView;
import com.yn.reader.model.common.Book;
import com.yn.reader.model.searchResult.SearchResultBook;
import com.yn.reader.util.Constant;
import com.yn.reader.util.SwipeRefreshLayoutUtil;
import com.yn.reader.view.adapter.BookListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchListActivity extends BaseMvpActivity implements SearchListView, SwipeRefreshLayout.OnRefreshListener {
    private SearchListPresenter mSearchListPresent;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.rv_list)
    RecyclerView rv_list;

    private List<Book> mBooks;
    private BookListAdapter mBookListAdapter;

    private String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_recycler_view);
        ButterKnife.bind(this);
        setToolbarTitle(R.string.search_result);
        keyword = getIntent().getStringExtra(Constant.KEY_WORD);

        SwipeRefreshLayoutUtil.styleSwipeRefreshLayout(swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        mSearchListPresent = new SearchListPresenter(this);
        mBooks = new ArrayList<>();
        mBookListAdapter = new BookListAdapter(this, mBooks);
        rv_list.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_line));
        rv_list.addItemDecoration(itemDecoration);
        rv_list.setAdapter(mBookListAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                loadData();
            }
        }, 400);

    }

    private void loadData() {
        if (!StringUtil.isEmpty(keyword)) {
            mSearchListPresent.searchBookByKeyword(keyword);
        }
    }

    @Override
    public BasePresenter getPresenter() {
        return mSearchListPresent;
    }

    @Override
    public Activity getContext() {
        return this;
    }

    @Override
    public void onLoadSearchResultHeatrank(List<SearchResultBook> heatrank) {
        swipeRefreshLayout.setRefreshing(false);

        if (heatrank == null || heatrank.size() == 0) {
            ToastUtil.showShort(this, getString(R.string.tip_search_no_result));
            return;
        }
        mBooks.clear();
        for (SearchResultBook bean : heatrank) {
            mBooks.add(new Book(bean));
        }
        mBookListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        loadData();
    }
}
