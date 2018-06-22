package com.yn.reader.mvp.views;



import com.yn.reader.model.home.ChangeBatchGroup;
import com.yn.reader.model.home.HomeGroup;

/**
 * Created by sunxy on 2017/12/26.
 */

public interface HomeView extends BaseView{

    void onDataLoaded(HomeGroup homeGroup);
    void onChangeBatchLoaded(ChangeBatchGroup changeBatchGroup);
//    void onHomeChannelLoaded(List<HomeBookMark> bookMarks);
//    void onNewsChannelLoaded(List<NewsChannel> channels);
//    void suggestResponse(SuggestResponse suggestResponse);
//    void onWeather(Weather weather);
//    void onComment(Comment comment);
}
