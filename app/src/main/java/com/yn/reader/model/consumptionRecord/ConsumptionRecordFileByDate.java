package com.yn.reader.model.consumptionRecord;

import com.hysoso.www.utillibrary.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luhe on 2018/3/21.
 */

public class ConsumptionRecordFileByDate {
    private String date;
    private List<ConsumptionRecord> mContain = new ArrayList<>();

    public ConsumptionRecordFileByDate(String date){
        this.date = date;
    }

    public ConsumptionRecordFileByDate(ConsumptionRecord consumptionRecord){
        this.date = TimeUtil.getDayDate(consumptionRecord.getCreatedate());
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void addRechargeRecord(ConsumptionRecord consumptionRecord){
        if (!consumptionRecord.getCreatedate().contains(date))return;
        if (mContain.contains(consumptionRecord))return;
        mContain.add(consumptionRecord);
    }
    public List<ConsumptionRecord> getContain(){
        return mContain;
    }
}
