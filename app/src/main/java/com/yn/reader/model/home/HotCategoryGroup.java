package com.yn.reader.model.home;

import java.util.List;

/**
 * Created by luhe on 2018/5/16.
 */

public class HotCategoryGroup {
    private List<HotCategory> mData;

    public HotCategoryGroup(List<HotCategory> data) {
        mData = data;
    }

    public void setData(List<HotCategory> data) {
        mData = data;
    }

    public List<HotCategory> getData() {
        return mData;
    }
}
