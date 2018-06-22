package com.yn.reader.model.rechargeRecord;

import com.hysoso.www.utillibrary.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luhe on 2018/3/21.
 */

public class RechargeRecordFileByDate {
    private String date;
    private List<RechargeRecord> mContain = new ArrayList<>();

    public RechargeRecordFileByDate(String date){
        this.date = date;
    }

    public RechargeRecordFileByDate(RechargeRecord rechargeRecord){
        this.date = TimeUtil.getDayDate(rechargeRecord.getCreatedate());
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void addRechargeRecord(RechargeRecord rechargeRecord){
        if (!rechargeRecord.getCreatedate().contains(date))return;
        if (mContain.contains(rechargeRecord))return;
        mContain.add(rechargeRecord);
    }
    public List<RechargeRecord> getContain(){
        return mContain;
    }
}
