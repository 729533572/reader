package com.yn.reader.mvp.views;

import com.yn.reader.model.hotSearch.HotSearch;

import java.util.List;

/**
 * Created by luhe on 2018/3/20.
 */

public interface SearchView extends BaseView{
    void onHotSearchKeyWordsLoaded(List<HotSearch> data);
    void cleanInputKeyword();
}
