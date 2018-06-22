package com.yn.reader.model.home;

import com.yn.reader.model.BaseData;

/**
 * 换一批
 * Created by sunxy on 2018/3/12.
 */

public class ChangeBatchGroup extends BaseData {
    private ChangeBatch data;

    public ChangeBatch getData() {
        return data;
    }

    public void setData(ChangeBatch data) {
        this.data = data;
    }
}
