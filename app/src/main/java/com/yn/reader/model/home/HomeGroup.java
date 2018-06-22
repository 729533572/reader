package com.yn.reader.model.home;

import com.yn.reader.model.BaseData;

/**
 * 首页导航所有信息
 * Created by sunxy on 2017/12/26.
 */

public class HomeGroup extends BaseData {
    private HomeList data;

    public HomeList getData() {
        return data;
    }

    public void setData(HomeList data) {
        this.data = data;
    }
}
