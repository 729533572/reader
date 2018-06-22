package com.yn.reader.mvp.presenters;

import com.yn.reader.mvp.views.BaseView;
import com.yn.reader.mvp.views.SearchListView;
import com.yn.reader.network.api.MiniRest;
import com.yn.reader.model.searchResult.SearchResultGroup;

/**
 * Created by luhe on 2018/3/21.
 */

public class SearchListPresenter extends BasePresenter{
    private SearchListView mSearchListView;
    private int pageno = 1;
    private int pagesize = 20;

    public SearchListPresenter(SearchListView searchListView){
        mSearchListView = searchListView;
    }
    @Override
    public BaseView getBaseView() {
        return mSearchListView;
    }

    @Override
    public void success(Object o) {
        if (o instanceof SearchResultGroup){
            mSearchListView.onLoadSearchResultHeatrank(((SearchResultGroup) o).getData().getHeatrank());
        }
    }

    @Override
    public void detachView() {
        unSubscribe();
    }

    public void searchBookByKeyword(String keyword) {
        addSubscription(MiniRest.getInstance().searchBookByKeyword(keyword,pageno,pagesize));
        pageno++;
    }
}
