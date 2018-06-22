package com.yn.reader.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hysoso.www.utillibrary.LogUtil;
import com.yn.reader.R;
import com.yn.reader.mvp.presenters.BasePresenter;
import com.yn.reader.mvp.presenters.BookListPresenter;
import com.yn.reader.mvp.views.BookListView;
import com.yn.reader.model.booklist.BookListGroup;
import com.yn.reader.model.booklist.NavigationCategory;
import com.yn.reader.model.common.Book;
import com.yn.reader.util.Constant;
import com.yn.reader.util.RecyclerViewUtil;
import com.yn.reader.util.SwipeRefreshLayoutUtil;
import com.yn.reader.view.adapter.BookListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 小说列表
 * Created by sunxy on 2018/2/22.
 */

public class BookListFragment extends BaseFragment implements BookListView, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.book_list_rec)
    RecyclerView recyclerView;

    private List<Book> bookList;
    private BookListAdapter bookListAdapter;
    private int categoryId;
    private int sex;
    private boolean isShowCategory;
    private BookListPresenter bookListPresenter;
    private int currentPage = 1;

    public static BookListFragment getInstance(int fragmentType, int categoryId, int sex) {

        Bundle bundle = new Bundle();
        bundle.putInt(Constant.KEY_FRAGMENT_TYPE, fragmentType);
        bundle.putInt(Constant.KEY_CATEGORY_ID, categoryId);
        bundle.putInt(Constant.KEY_CATEGORY_SEX, sex);

        BookListFragment fragment = new BookListFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);
        ButterKnife.bind(this, view);
        SwipeRefreshLayoutUtil.styleSwipeRefreshLayout(swipeRefreshLayout);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bookListPresenter = new BookListPresenter(this);

        Bundle bundle = getArguments();
        categoryId = bundle.getInt(Constant.KEY_CATEGORY_ID, 0);
        sex = bundle.getInt(Constant.KEY_CATEGORY_SEX, 0);
        isShowCategory = bundle.getInt(Constant.KEY_FRAGMENT_TYPE, Constant.FRAGMENT_CATEGORY_DETAIL) == Constant.FRAGMENT_CATEGORY_DETAIL;
        bookList = new ArrayList<>();
        bookListAdapter = new BookListAdapter(getActivity(), bookList);
        bookListAdapter.setOnCategoryChanged(new BookListAdapter.OnCategoryChanged() {
            @Override
            public void categoryChanged(String tags, Integer chargetype, Integer status, Integer word, Integer lastnevigate) {
                LogUtil.e(getClass().getSimpleName(), "categoryChanged");
                bookListPresenter.getBooksByCategory(categoryId, sex, 20, currentPage, status, chargetype, lastnevigate, word, tags);
            }
        });
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        RecyclerViewUtil.setDecoration(recyclerView, DividerItemDecoration.VERTICAL);


        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(bookListAdapter);

        if (isShowCategory) {
            bookListPresenter.getNavigationCategory(categoryId);
        }

        swipeRefreshLayout.setOnRefreshListener(this);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                bookListPresenter.getBooksByCategory(categoryId, sex, 20, currentPage);
                handler.removeCallbacks(this);
            }
        }, Constant.TIME_DELAY);
    }


    @Override
    public void onBookListLoaded(BookListGroup bookListGroup) {
        swipeRefreshLayout.setRefreshing(false);
        if (bookListGroup.getHeatrank() != null) {
            bookList.clear();
            bookList.addAll(bookListGroup.getHeatrank());
            bookListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCategoryLoaded(NavigationCategory navigationCategory) {
        bookListAdapter.setCategory(navigationCategory);
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
                bookListPresenter.getBooksByCategory(categoryId, sex, 20, currentPage);
                handler.removeCallbacks(this);
            }
        }, Constant.TIME_DELAY);
    }
}
