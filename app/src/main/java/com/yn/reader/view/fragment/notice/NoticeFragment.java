package com.yn.reader.view.fragment.notice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hysoso.www.utillibrary.LogUtil;
import com.yn.reader.R;
import com.yn.reader.login.utils.LoadingUtils;
import com.yn.reader.login.utils.ToastUtils;
import com.yn.reader.mvp.presenters.BasePresenter;
import com.yn.reader.mvp.presenters.NoticePresenter;
import com.yn.reader.mvp.views.NoticeView;
import com.yn.reader.model.BaseData;
import com.yn.reader.model.notice.Notice;
import com.yn.reader.model.notice.NoticeGroup;
import com.yn.reader.util.Constant;
import com.yn.reader.util.EditListener;
import com.yn.reader.util.RecyclerViewUtil;
import com.yn.reader.util.SwipeRefreshLayoutUtil;
import com.yn.reader.view.NoticeDetailActivity;
import com.yn.reader.view.adapter.NoticeAdapter;
import com.yn.reader.view.adapter.NoticePagerAdapter;
import com.yn.reader.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 分类
 * Created by sunxy on 2018/2/7.
 */

public class NoticeFragment extends BaseFragment implements NoticeView, EditListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String KEY_TYPE = "key_type";

    //    @BindView(R.id.category_rec)

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.rv_notice)
    RecyclerView recyclerView;

    @BindView(R.id.iv_tip)
    ImageView iv_tip;

    private NoticeAdapter mNoticeAdapter;
    private List<Notice> mNoticeList;

    private int noticeType;
    private NoticePresenter noticePresenter;
    private boolean isDeleteOperation;
    private boolean isSelectAll;

    public List<Notice> getNoticeList() {
        return mNoticeList;
    }

    public static NoticeFragment getInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, type);
        NoticeFragment fragment = new NoticeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice, container, false);
        ButterKnife.bind(this, view);
        SwipeRefreshLayoutUtil.styleSwipeRefreshLayout(swipeRefreshLayout);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerViewUtil.setDecoration(recyclerView, DividerItemDecoration.VERTICAL);
        mNoticeList = new ArrayList<>();

        mNoticeAdapter = new NoticeAdapter(getActivity(), mNoticeList);
        mNoticeAdapter.setOnItemClick(new NoticeAdapter.OnItemClick() {
            @Override
            public void click(int position) {
                int type = getArguments().getInt(KEY_TYPE);
                Notice notice = mNoticeList.get(position);
                Intent intent = new Intent(getContext(), NoticeDetailActivity.class);
                intent.putExtra(Constant.KEY_TYPE, type);
                intent.putExtra(Constant.KEY_MESSAGE_CONTENT, notice);
                getContext().startActivity(intent);
                notice.setSelect(true);
                mark2Read();
            }
        });
        recyclerView.setAdapter(mNoticeAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);
        final Handler handler = new Handler();
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
        noticePresenter = new NoticePresenter(this);
        noticeType = getArguments().getInt(KEY_TYPE, NoticePagerAdapter.PAGER_NOTICE_SYSTEM);
        noticePresenter.getNotice(noticeType);
    }


    @Override
    public BasePresenter getPresenter() {
        return noticePresenter;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public void onNoticeLoaded(NoticeGroup noticeGroup) {
        swipeRefreshLayout.setRefreshing(false);
        if (noticeGroup.getData() != null) {
            mNoticeList.clear();
            mNoticeList.addAll(noticeGroup.getData());
            mNoticeAdapter.notifyDataSetChanged();
        }
        iv_tip.setVisibility(mNoticeList.size() > 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onNoticeMarkOrDeleted(BaseData baseData) {
        if (baseData.getStatus() == 1) {
            if (isDeleteOperation)
                delete();
            else mark();
        }
        unSelectAll();
        LoadingUtils.hideLoading();
    }

    private void mark() {
        for (Notice notice : mNoticeList) {
            if (notice.isSelect()) {
                notice.setStatus(1);
            }
        }
        mNoticeAdapter.notifyDataSetChanged();
    }

    private void delete() {
        List<Notice> remainNotices = new ArrayList<>();
        for (Notice notice : mNoticeList) {
            if (!notice.isSelect()) {
                remainNotices.add(notice);
            }
        }
        mNoticeList.clear();
        mNoticeList.addAll(remainNotices);
        mNoticeAdapter.notifyDataSetChanged();
    }

    private void unSelectAll() {
        isSelectAll = false;
        for (Notice notice : mNoticeList) {
            notice.setSelect(false);
        }
        mNoticeAdapter.notifyDataSetChanged();
    }

    private void selectAll() {
        isSelectAll = !isSelectAll;
        for (Notice notice : mNoticeList) {
            notice.setSelect(isSelectAll);
        }
        mNoticeAdapter.notifyDataSetChanged();
    }

    @Override
    public Activity getContext() {
        return getActivity();
    }

    @Override
    public void onEditClicked() {
        mNoticeAdapter.setEditModel(true);
        LogUtil.e(getClass().getSimpleName(), mNoticeList.size() + "");
        mNoticeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCompleteClicked() {
        mNoticeAdapter.setEditModel(false);
        mNoticeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSelectAllChecked() {
        selectAll();
    }

    @Override
    public void deleteSelect() {
        LoadingUtils.showLoading();
        isDeleteOperation = true;
        String messageId = getSelectMessageId();

        if (!TextUtils.isEmpty(messageId)) {
            noticePresenter.markToReadOrDelete(Constant.TYPE_DELETE, messageId);
        } else {
            ToastUtils.showLong(getActivity(), R.string.select_nothing);
        }
    }

    @Override
    public void mark2Read() {
        isDeleteOperation = false;
        String messageId = getSelectMessageId();
        if (!TextUtils.isEmpty(messageId)) {
            noticePresenter.markToReadOrDelete(Constant.TYPE_MARK_READ, messageId);
        } else ToastUtils.showLong(getActivity(), R.string.select_nothing);
    }

    @Override
    public void refresh() {

    }

    @NonNull
    private String getSelectMessageId() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Notice notice : mNoticeList) {
            if (notice.isSelect()) {
                stringBuilder.append(notice.getId()).append(",");
            }
        }
        if (stringBuilder.toString().length() > 1) {
            return stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);
        }
        return "";
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
