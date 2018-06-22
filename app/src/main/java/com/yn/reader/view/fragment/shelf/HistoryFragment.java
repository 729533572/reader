package com.yn.reader.view.fragment.shelf;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hysoso.www.utillibrary.LogUtil;
import com.yn.reader.R;
import com.yn.reader.model.common.Book;
import com.yn.reader.model.common.BookDBManager;
import com.yn.reader.util.Constant;
import com.yn.reader.view.MainActivity;
import com.yn.reader.view.adapter.MainPagerAdapter;

import butterknife.OnClick;


/**
 * 书架-收藏-历史
 * Created by sunxy on 2018/2/8.
 */

public class HistoryFragment extends ShelfItemFragment {
    private final int SPAN_COUNT = 3;

    private boolean isSelectAll;

    public static HistoryFragment getInstance() {
        Bundle bundle = new Bundle();
        HistoryFragment fragment = new HistoryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    void loadData() {
        bookList.clear();
        bookList.addAll(BookDBManager.getInstance().getHistories());
        shelfAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);

        showTip();
    }

    private void showTip() {
        if (bookList.size() > 0) fl_tip.setVisibility(View.GONE);
        else {
            iv_tip.setImageResource(R.mipmap.ic_tip_shelf_history);
            tv_tip.setText(R.string.tip_no_reading_history);
            fl_tip.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onEditClicked() {
        LogUtil.i(getClass().getSimpleName(), "onEditClicked");
        shelfAdapter.setEditModel(true);
    }

    @Override
    public void onCompleteClicked() {
        LogUtil.i(getClass().getSimpleName(), "onCompleteClicked");
        shelfAdapter.setEditModel(false);
    }

    @Override
    public void onSelectAllChecked() {
        LogUtil.i(getClass().getSimpleName(), "onSelectAllChecked");
        isSelectAll = !isSelectAll;
        for (Book book : bookList) {
            if (book != null) {
                book.setSelect(isSelectAll);
            }
        }
        shelfAdapter.notifyDataSetChanged();
        if (shelfAdapter.getSelections().size() == shelfAdapter.getItemCount()) {
            if (getContext() instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) getContext();
                mainActivity.setBottomLeft("取消");
            }
        } else {
            if (getContext() instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) getContext();
                mainActivity.setBottomLeft("全选");
            }
        }
    }

    @Override
    public void deleteSelect() {
        LogUtil.i(getClass().getSimpleName(), "deleteSelect");
        if (!shelfAdapter.hasSelection()) return;
        new MaterialDialog.Builder(getContext())
                .content(R.string.delete_selection)
                .canceledOnTouchOutside(false)
                .negativeText(R.string.cancel)
                .positiveText(R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        makeTrueDeleteSelection();
                    }
                }).show();


    }

    /**
     * 确定删除所选
     */
    public void makeTrueDeleteSelection() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (Book bean : shelfAdapter.getSelections()) {
                    BookDBManager.getInstance().deleteHistory(bean.getBookid());
                }
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        shelfAdapter.deleteSelections();
                        showTip();
                    }
                });
            }
        }).start();
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
