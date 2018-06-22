package com.yn.reader.model.consumptionRecord;

import com.yn.reader.model.BaseData;

import java.util.List;

/**
 * Created by luhe on 2018/3/21.
 */

public class ConsumptionRecordGroup extends BaseData{
    private List<ConsumptionRecord> shoplist;

    public void setShoplist(List<ConsumptionRecord> shoplist) {
        this.shoplist = shoplist;
    }

    public List<ConsumptionRecord> getShoplist() {
        return shoplist;
    }
}
