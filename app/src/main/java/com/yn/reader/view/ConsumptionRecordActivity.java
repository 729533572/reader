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
import com.yn.reader.mvp.presenters.ConsumptionRecordPresenter;
import com.yn.reader.mvp.views.ConsumptionRecordView;
import com.yn.reader.model.consumptionRecord.ConsumptionRecord;
import com.yn.reader.util.SwipeRefreshLayoutUtil;
import com.yn.reader.view.adapter.ConsumptionRecordFileAdapter;
import com.yn.reader.widget.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConsumptionRecordActivity extends BaseMvpActivity implements ConsumptionRecordView, SwipeRefreshLayout.OnRefreshListener {
    private ConsumptionRecordPresenter mConsumptionRecordPresenter;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;
    private List<ConsumptionRecord> mConsumptionRecords;
    private ConsumptionRecordFileAdapter mConsumptionRecordFileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_recycler_view);
        ButterKnife.bind(this);
        setToolbarTitle(R.string.consumption_record);
        SwipeRefreshLayoutUtil.styleSwipeRefreshLayout(swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        mConsumptionRecords = new ArrayList<>();
        mConsumptionRecordFileAdapter = new ConsumptionRecordFileAdapter(this, mConsumptionRecords);

        final FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_line));
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setAdapter(mConsumptionRecordFileAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                loadData();
            }
        }, 400);
    }

    private void loadData() {
        mConsumptionRecordPresenter = new ConsumptionRecordPresenter(this);
        mConsumptionRecordPresenter.getConsumptionRecord();
    }

    @Override
    public BasePresenter getPresenter() {
        return mConsumptionRecordPresenter;
    }

    @Override
    public Activity getContext() {
        return this;
    }

    @Override
    public void onLoadConsumptionRecords(List<ConsumptionRecord> shoplist) {
        swipeRefreshLayout.setRefreshing(false);

        if (shoplist == null || shoplist.size() == 0) return;
        mConsumptionRecords.clear();
        mConsumptionRecords.addAll(shoplist);
        mConsumptionRecordFileAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        loadData();
    }
}
