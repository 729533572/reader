package com.yn.reader.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;

import com.yn.reader.R;
import com.yn.reader.base.BaseMvpActivity;
import com.yn.reader.mvp.presenters.BasePresenter;
import com.yn.reader.mvp.presenters.RechargeRecordPresent;
import com.yn.reader.mvp.views.RechargeRecordView;
import com.yn.reader.model.rechargeRecord.RechargeRecord;
import com.yn.reader.util.SwipeRefreshLayoutUtil;
import com.yn.reader.view.adapter.RechargeRecordFileAdapter;
import com.yn.reader.widget.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RechargeRecordActivity extends BaseMvpActivity implements RechargeRecordView, SwipeRefreshLayout.OnRefreshListener {
    private RechargeRecordPresent mRechargeRecordPresent;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;
    List<RechargeRecord> mRechargeRecords;
    private RechargeRecordFileAdapter mRechargeRecordFileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_recycler_view);
        ButterKnife.bind(this);
        SwipeRefreshLayoutUtil.styleSwipeRefreshLayout(swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        setToolbarTitle(R.string.recharge_record);

        mRechargeRecordPresent = new RechargeRecordPresent(this);
        mRechargeRecords = new ArrayList<>();
        mRechargeRecordFileAdapter = new RechargeRecordFileAdapter(this, mRechargeRecords);

        final FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_line));
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setAdapter(mRechargeRecordFileAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                loadData();
            }
        }, 400);
    }

    private void loadData() {
        mRechargeRecordPresent.getRechargeRecord();
    }

    @Override
    public BasePresenter getPresenter() {
        return mRechargeRecordPresent;
    }

    @Override
    public Activity getContext() {
        return this;
    }

    @Override
    public void onLoadRechargeRecord(List<RechargeRecord> paylist) {
        swipeRefreshLayout.setRefreshing(false);

        if (paylist == null || paylist.size() == 0) return;
        mRechargeRecords.clear();
        mRechargeRecords.addAll(paylist);
        mRechargeRecordFileAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        loadData();
    }
}
