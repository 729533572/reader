package com.yn.reader.mvp.views;

import com.yn.reader.model.searchResult.SearchResultBook;

import java.util.List;

/**
 * Created by luhe on 2018/3/21.
 */

public interface SearchListView extends BaseView{
    void onLoadSearchResultHeatrank(List<SearchResultBook> heatrank);
}
