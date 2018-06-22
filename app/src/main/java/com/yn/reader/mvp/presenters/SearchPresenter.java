package com.yn.reader.mvp.presenters;

import com.hysoso.www.utillibrary.LogUtil;
import com.yn.reader.db.SearchHistoryBeanDao;
import com.yn.reader.mvp.views.BaseView;
import com.yn.reader.mvp.views.SearchView;
import com.yn.reader.network.api.MiniRest;
import com.yn.reader.model.dao.DbHelper;
import com.yn.reader.model.dao.SearchHistoryBean;
import com.yn.reader.model.hotSearch.HotSearch;
import com.yn.reader.model.hotSearch.HotSearchGroup;
import com.yn.reader.util.IntentUtils;
import com.yn.reader.view.SearchListActivity;
import com.yn.reader.view.SharedWebActivity;

import java.util.List;

/**
 * Created by luhe on 2018/3/20.
 */

public class SearchPresenter extends BasePresenter {
    private static int MAX_HISTORY_SIZE = 10;
    private SearchView mSearchView;

    public SearchPresenter(SearchView searchView) {
        this.mSearchView = searchView;
    }

    @Override
    public BaseView getBaseView() {
        return mSearchView;
    }

    @Override
    public void success(Object o) {
        if (o instanceof HotSearchGroup) {
            mSearchView.onHotSearchKeyWordsLoaded(((HotSearchGroup) o).getData());
        }
    }

    @Override
    public void detachView() {
        unSubscribe();
    }

    public void getHotSearchkeyWords() {
        addSubscription(MiniRest.getInstance().getHotSearchKeyWords());
    }

    public void searchByHistory(SearchHistoryBean searchHistoryBean) {
        /*
        *1.关键字 2.web广告 3.appstore 4.Android 5.某本书id
        * */
        switch (searchHistoryBean.getLink_type()) {
            case 1:
                searchByKeyWord(searchHistoryBean.getName());
                break;
            case 2:
                IntentUtils.startActivity(mSearchView.getContext(), SharedWebActivity.class, searchHistoryBean.getLink_content(), searchHistoryBean.getName());
                break;
            case 3:
                break;
            case 4:
                IntentUtils.startActivity(mSearchView.getContext(), SharedWebActivity.class, searchHistoryBean.getLink_content(), searchHistoryBean.getName());
                break;
            case 5:
                IntentUtils.startBookDetailActivity(mSearchView.getContext(), Long.parseLong(searchHistoryBean.getLink_content()));
                break;
        }
    }

    public void searchByKeyWord(String keyword) {
        try {
            SearchHistoryBeanDao searchHistoryBeanDao = DbHelper.getInstance().getDaoSession().getSearchHistoryBeanDao();
            int size = searchHistoryBeanDao.loadAll().size();
            if (size >= MAX_HISTORY_SIZE) {
                List<SearchHistoryBean> deleteSearchHistories = searchHistoryBeanDao.queryBuilder().orderAsc(SearchHistoryBeanDao.Properties.Id).limit(size - MAX_HISTORY_SIZE + 1).build().list();
                for (SearchHistoryBean bean : deleteSearchHistories) {
                    searchHistoryBeanDao.delete(bean);
                }
            }
            searchHistoryBeanDao.insertOrReplace(new SearchHistoryBean(keyword));
        } catch (Exception ex) {
            LogUtil.e("searchByKeyWord:" + keyword, ex.getMessage() + "/" + ex.getCause());
        }
        IntentUtils.startActivity(mSearchView.getContext(), SearchListActivity.class, keyword);
        mSearchView.cleanInputKeyword();
    }


    public void searchByHotSearch(HotSearch hotSearch) {
      /*
        *1.关键字 2.web广告 3.appstore 4.Android 5.某本书id
        * */
        switch (hotSearch.getLink_type()) {
            case 1:
                searchByKeyWord(hotSearch.getName());
                break;
            case 2:
                IntentUtils.startActivity(mSearchView.getContext(), SharedWebActivity.class, hotSearch.getLink_content(), hotSearch.getName());
                break;
            case 3:
                break;
            case 4:
                IntentUtils.startActivity(mSearchView.getContext(), SharedWebActivity.class, hotSearch.getLink_content(), hotSearch.getName());
                break;
            case 5:
                IntentUtils.startBookDetailActivity(mSearchView.getContext(), Long.parseLong(hotSearch.getLink_content()));
                break;
        }
        SearchHistoryBeanDao searchHistoryBeanDao = DbHelper.getInstance().getDaoSession().getSearchHistoryBeanDao();
        searchHistoryBeanDao.insertOrReplace(new SearchHistoryBean(hotSearch));
    }
}
