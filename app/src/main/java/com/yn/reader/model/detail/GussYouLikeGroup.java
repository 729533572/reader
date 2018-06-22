package com.yn.reader.model.detail;

import com.yn.reader.model.BaseData;

/**
 * 猜你喜欢
 * Created by sunxy on 2018/3/12.
 */

public class GussYouLikeGroup extends BaseData {
    private GussLike data;

    public GussLike getData() {
        return data;
    }

    public void setData(GussLike data) {
        this.data = data;
    }
}
