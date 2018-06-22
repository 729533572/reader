package com.yn.reader.model.searchResult;

import com.yn.reader.model.BaseData;

/**
 * Created by luhe on 2018/3/21.
 */

public class SearchResultGroup extends BaseData{
    private SearchResult data;

    public void setData(SearchResult data) {
        this.data = data;
    }

    public SearchResult getData() {
        return data;
    }
}
