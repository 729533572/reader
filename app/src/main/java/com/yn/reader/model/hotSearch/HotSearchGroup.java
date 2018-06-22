package com.yn.reader.model.hotSearch;

import com.yn.reader.model.BaseData;

import java.util.List;

/**
 * Created by luhe on 2018/3/20.
 */

public class HotSearchGroup extends BaseData{
    private List<HotSearch> data;
    public void setData(List<HotSearch> data) {
        this.data = data;
    }

    public List<HotSearch> getData() {
        return data;
    }
}
