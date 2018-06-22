package com.yn.reader.mvp.views;

import com.yn.reader.model.rechargeRecord.RechargeRecord;

import java.util.List;

/**
 * Created by luhe on 2018/3/21.
 */

public interface RechargeRecordView extends BaseView{
    void onLoadRechargeRecord(List<RechargeRecord> paylist);
}
