package com.yn.reader.model.rechargeRecord;

import com.yn.reader.model.BaseData;

import java.util.List;

/**
 * Created by luhe on 2018/3/21.
 */

public class RechargeRecordGroup extends BaseData{
    private List<RechargeRecord> paylist;

    public void setPaylist(List<RechargeRecord> paylist) {
        this.paylist = paylist;
    }

    public List<RechargeRecord> getPaylist() {
        return paylist;
    }
}
