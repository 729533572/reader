package com.yn.reader.model.adconfig;

import com.yn.reader.model.BaseData;

import java.util.List;

/**
 * 广告配置
 * Created by sunxy on 2018/1/10.
 */

public class AdConfigResponse extends BaseData {
    private List<AdConfig>data;

    public List<AdConfig> getData() {
        return data;
    }

    public void setData(List<AdConfig> data) {
        this.data = data;
    }
}
