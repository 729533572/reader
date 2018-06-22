package com.yn.reader.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;

import com.yn.reader.R;
import com.yn.reader.base.BaseMvpActivity;
import com.yn.reader.login.utils.LoadingUtils;
import com.yn.reader.mvp.presenters.BasePresenter;
import com.yn.reader.mvp.presenters.CommentHotMorePresenter;
import com.yn.reader.mvp.views.CommentHotMoreView;
import com.yn.reader.model.comment.Comment;
import com.yn.reader.model.comment.CommentGroup;
import com.yn.reader.util.Constant;
import com.yn.reader.util.SwipeRefreshLayoutUtil;
import com.yn.reader.view.adapter.CommentAdapter;
import com.yn.reader.widget.FullyLinearLayoutManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentHotMoreActivity extends BaseMvpActivity implements CommentHotMoreView, CommentAdapter.OnCommentItemClick, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.rv_list)
    RecyclerView rv_list;

    private long bookId;
    private static int pageSize = 20;
    private CommentHotMorePresenter mCommentHotMorePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_recycler_view);
        ButterKnife.bind(this);
        setToolbarTitle("更多热评");

        SwipeRefreshLayoutUtil.styleSwipeRefreshLayout(swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_line));
        rv_list.addItemDecoration(itemDecoration);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                loadData();
            }
        }, 400);
    }

    private void loadData() {
        bookId = getIntent().getLongExtra(Constant.KEY_ID, 0);
        LoadingUtils.showLoading(this);
        mCommentHotMorePresenter = new CommentHotMorePresenter(this);
        mCommentHotMorePresenter.getHotComments(bookId, pageSize);
    }

    @Override
    public BasePresenter getPresenter() {
        return mCommentHotMorePresenter;
    }

    @Override
    public Activity getContext() {
        return this;
    }

    @Override
    public void onCommentsLoaded(CommentGroup commentGroup) {
        swipeRefreshLayout.setRefreshing(false);
        if (commentGroup.getComments() != null && !commentGroup.getComments().isEmpty()) {
            final FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(this);
            switch (commentGroup.getType()) {
                case Constant.SORT_TYPE_HOT:
                    rv_list.setHasFixedSize(false);
                    rv_list.setNestedScrollingEnabled(false);
                    rv_list.setLayoutManager(layoutManager);
                    rv_list.setAdapter(new CommentAdapter(this, commentGroup.getComments())
                            .setShowLikeAndReport(true).setOnCommentItemClick(this));
                    break;
            }
        }
    }

    @Override
    public void like(Comment comment) {
        mCommentHotMorePresenter.like(comment.getCommentid());
    }

    @Override
    public void report(Comment comment) {
        mCommentHotMorePresenter.report(comment.getCommentid());
    }

    @Override
    public void onRefresh() {
        loadData();
    }
}
