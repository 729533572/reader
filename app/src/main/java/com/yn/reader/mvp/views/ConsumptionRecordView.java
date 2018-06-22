package com.yn.reader.mvp.views;

import com.yn.reader.model.consumptionRecord.ConsumptionRecord;

import java.util.List;

/**
 * Created by luhe on 2018/3/21.
 */

public interface ConsumptionRecordView extends BaseView{
    void onLoadConsumptionRecords(List<ConsumptionRecord> shoplist);
}
