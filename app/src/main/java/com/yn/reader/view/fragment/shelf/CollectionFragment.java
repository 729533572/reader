package com.yn.reader.view.fragment.shelf;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hysoso.www.utillibrary.LogUtil;
import com.yn.reader.R;
import com.yn.reader.mvp.presenters.BasePresenter;
import com.yn.reader.mvp.presenters.ShelfPresenter;
import com.yn.reader.mvp.views.ShelfView;
import com.yn.reader.model.common.Book;
import com.yn.reader.model.common.BookDBManager;
import com.yn.reader.model.favorite.FavoriteGroup;
import com.yn.reader.util.Constant;
import com.yn.reader.util.UserInfoManager;
import com.yn.reader.view.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * 书架-收藏-历史
 * Created by sunxy on 2018/2/8.
 */

public class CollectionFragment extends ShelfItemFragment implements ShelfView {
    private final int SPAN_COUNT = 3;
    private ShelfPresenter mShelfPresenter;

    private boolean isSelectAll;

    public static CollectionFragment getInstance() {
        Bundle bundle = new Bundle();
        CollectionFragment fragment = new CollectionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mShelfPresenter = new ShelfPresenter(this);
    }

    @Override
    void loadData() {
        if (UserInfoManager.getInstance().isLanded())
            mShelfPresenter.getFavorite();
        else {
            swipeRefreshLayout.setRefreshing(false);
            bookList.clear();
            bookList.addAll(BookDBManager.getInstance().getCollections());
            shelfAdapter.notifyDataSetChanged();

            if (bookList.size() > 0) iv_tip.setVisibility(View.GONE);
            else {
                iv_tip.setImageResource(R.mipmap.ic_tip_shelf_collection);
                iv_tip.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public BasePresenter getPresenter() {
        return mShelfPresenter;
    }

    @Override
    public Activity getContext() {
        return getActivity();
    }

    @Override
    public void onCollectionLoaded(final FavoriteGroup favoriteGroup) {
        swipeRefreshLayout.setRefreshing(false);
        if (favoriteGroup.getData() != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (this) {
                        List<Book> books = BookDBManager.getInstance().getCollections();
                        List<Book> allBooks = synchronizeData(books, favoriteGroup.getData());
                        bookList.clear();
                        bookList.addAll(allBooks);
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                shelfAdapter.notifyDataSetChanged();

                                showTip();
                            }
                        });
                        for (final Book bean : allBooks) {
                            if (!isContain(books, bean)) {
                                BookDBManager.getInstance().collect(bean, true);
                            }
                            if (!isContain(favoriteGroup.getData(), bean)) {
                                BookDBManager.getInstance().setBookUploaded(bean.getBookid(), false);
                            }
                        }
                    }
                }
            }).start();
        }
    }

    /**
     * 数组的并集计算
     *
     * @param list1
     * @param list2
     * @return
     */
    private List<Book> synchronizeData(List<Book> list1, List<Book> list2) {
        List<Book> list = list1 != null ? list1 : new ArrayList<Book>();
        for (Book bean : list2) {
            if (!isContain(list, bean)) list.add(bean);
        }
        return list;
    }

    /**
     * 判断数组是否包含一个项
     *
     * @param list
     * @param book
     * @return
     */
    private boolean isContain(List<Book> list, Book book) {
        boolean isContain = false;
        for (Book bean : list) {
            if (bean.getBookid() == book.getBookid()) {
                isContain = true;
                break;
            }
        }
        return isContain;
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
                mShelfPresenter.deleteFavoriteSelections(shelfAdapter.getSelections());
            }
        }).start();
    }

    @Override
    public void onDeleteCollectionSelection() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (Book bean : shelfAdapter.getSelections()) {
                    BookDBManager.getInstance().deleteCollection(bean.getBookid());
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

    private void showTip() {
        if (bookList.size() > 0) fl_tip.setVisibility(View.GONE);
        else {
            iv_tip.setImageResource(R.mipmap.ic_tip_shelf_collection);
            tv_tip.setText(R.string.tip_no_reading_collection);
            fl_tip.setVisibility(View.VISIBLE);
        }
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
