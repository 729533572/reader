package com.yn.reader.view.fragment.shelf;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hysoso.www.utillibrary.LogUtil;
import com.yn.reader.R;
import com.yn.reader.model.common.BookDBManager;
import com.yn.reader.mvp.presenters.BasePresenter;
import com.yn.reader.model.chapter.ChapterListBean;
import com.yn.reader.model.common.Book;
import com.yn.reader.util.ComUtils;
import com.yn.reader.util.Constant;
import com.yn.reader.util.EditListener;
import com.yn.reader.util.IntentUtils;
import com.yn.reader.util.SwipeRefreshLayoutUtil;
import com.yn.reader.util.UserInfoManager;
import com.yn.reader.view.LoginTipActivity;
import com.yn.reader.view.MainActivity;
import com.yn.reader.view.ReaderActivity;
import com.yn.reader.view.adapter.MainPagerAdapter;
import com.yn.reader.view.adapter.OnItemClickListener;
import com.yn.reader.view.adapter.ShelfAdapter;
import com.yn.reader.view.fragment.BaseFragment;
import com.yn.reader.widget.PostItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 书架-收藏-历史
 * Created by sunxy on 2018/2/8.
 */

public abstract class ShelfItemFragment extends BaseFragment implements EditListener, SwipeRefreshLayout.OnRefreshListener {
    private final int SPAN_COUNT = 3;

    @BindView(R.id.fl_tip)
    protected ViewGroup fl_tip;

    @BindView(R.id.iv_tip)
    protected ImageView iv_tip;

    @BindView(R.id.tv_tip)
    protected TextView tv_tip;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.rv_shelf)
    RecyclerView recyclerView;

    protected ShelfAdapter shelfAdapter;
    protected List<Book> bookList;

    public List<Book> getBookList() {
        return bookList;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shelf, container, false);
        ButterKnife.bind(this, view);
        view.findViewById(R.id.btn_find_book).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.onPageSelected(MainPagerAdapter.PAGER_HOME);
            }
        });
        SwipeRefreshLayoutUtil.styleSwipeRefreshLayout(swipeRefreshLayout);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LogUtil.i(getClass().getSimpleName(), "onViewCreated");
        bookList = new ArrayList<>();
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
        recyclerView.setLayoutManager(layoutManager);
        shelfAdapter = new ShelfAdapter(getActivity(), bookList);

        shelfAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void clickItem(Object item) {
                Book book = (Book) item;
                boolean isFreeLastChapter = false;
                for (ChapterListBean bean : book.getChapterlist()) {
                    if (bean.getChapterid() == book.getChapterid()) {
                        if (bean.getChaptershoptype() == 1) {
                            isFreeLastChapter = true;
                            break;
                        }
                    }
                }
                if (!UserInfoManager.getInstance().isLanded() && !isFreeLastChapter) {
                    IntentUtils.startActivity(getContext(), LoginTipActivity.class);
                    return;
                }
                BookDBManager.getInstance().addToHistory(book);
                IntentUtils.startActivity(getContext(), ReaderActivity.class, book.getBookid());
            }
        });
        recyclerView.setAdapter(shelfAdapter);

        GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        };
        layoutManager.setSpanSizeLookup(spanSizeLookup);
        recyclerView.addItemDecoration(new PostItemDecoration(ComUtils.dip2px(17), spanSizeLookup));

        swipeRefreshLayout.setOnRefreshListener(this);
        super.onViewCreated(view, savedInstanceState);
    }

    abstract void loadData();


    @Override
    public void onResume() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                loadData();
                handler.removeCallbacks(this);
            }
        }, Constant.TIME_DELAY);
        super.onResume();
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public Activity getContext() {
        return getActivity();
    }


    @Override
    public void mark2Read() {

    }

    @Override
    public void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        loadData();
    }

    @Override
    public void onRefresh() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LogUtil.i(getClass().getSimpleName(), "onRefresh run");
                loadData();
                handler.removeCallbacks(this);
            }
        }, Constant.TIME_DELAY);
    }
}
