package com.yn.reader.mvp.presenters;


import com.yn.reader.mvp.views.BaseView;
import com.yn.reader.mvp.views.HomeView;
import com.yn.reader.network.api.MiniRest;
import com.yn.reader.model.home.ChangeBatchGroup;
import com.yn.reader.model.home.HomeGroup;

/**
 * 首页数据请求以及逻辑处理
 * Created by sunxy on 2017/12/26.
 */

public class HomePresenter extends BasePresenter{
    private HomeView homeView;

    public HomePresenter(HomeView homeView) {
        this.homeView = homeView;
    }

    public void getHomePageInfo(int type) {
        addSubscription(MiniRest.getInstance().getHomePageInfo(type));

    }

    public void changeBatch(int homechannelid, int pagesize, int current_page) {
        addSubscription(MiniRest.getInstance().changeBatch(homechannelid, pagesize, current_page));
    }

    public void getHomeChannel() {
        //addSubscription(MiniRest.getInstance().getHomePanelDefaultWebsites());

    }


    public void clickEvent(String type) {
        addSubscription(MiniRest.getInstance().clickEvent(type));

    }

    public void searchRecord(String keywords) {
        addSubscription(MiniRest.getInstance().searchRecord(keywords));

    }

    public void activeRecord() {
        addSubscription(MiniRest.getInstance().appActive());

    }


    @Override
    public BaseView getBaseView() {
        return homeView;
    }

    @Override
    public void success(Object o) {
        if (o instanceof HomeGroup) {
            HomeGroup homeGroup = (HomeGroup) o;
            homeView.onDataLoaded(homeGroup);
        } else if (o instanceof ChangeBatchGroup) {
            ChangeBatchGroup changeBatchGroup = (ChangeBatchGroup) o;
            homeView.onChangeBatchLoaded(changeBatchGroup);
        }
    }

    @Override
    public void detachView() {
        unSubscribe();
    }
}
