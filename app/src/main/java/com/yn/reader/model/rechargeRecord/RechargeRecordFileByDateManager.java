package com.yn.reader.model.rechargeRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luhe on 2018/3/21.
 */

public class RechargeRecordFileByDateManager {
    private static RechargeRecordFileByDateManager mSingleInstance;
    private List<RechargeRecordFileByDate> mRechargeRecordFilesByDate;
    private RechargeRecordFileByDateManager(){}

    public static RechargeRecordFileByDateManager getInstance() {
        if (mSingleInstance==null){
         synchronized (RechargeRecordFileByDateManager.class){
             if (mSingleInstance==null)mSingleInstance = new RechargeRecordFileByDateManager();
         }
        }
        return mSingleInstance;
    }
    public List<RechargeRecordFileByDate> getRechargeRecordFilesByDate(List<RechargeRecord> rechargeRecords){
        mRechargeRecordFilesByDate = new ArrayList<>();

        for (RechargeRecord bean:rechargeRecords) {
            RechargeRecordFileByDate file = getFileByRechargeRecord(bean);
            file.addRechargeRecord(bean);
        }

        return mRechargeRecordFilesByDate;
    }
    private RechargeRecordFileByDate getFileByRechargeRecord(RechargeRecord rechargeRecord){
        RechargeRecordFileByDate rechargeRecordFileByDate = null;

        for (RechargeRecordFileByDate bean:mRechargeRecordFilesByDate) {
            if (rechargeRecord.getCreatedate().contains(bean.getDate())){
                rechargeRecordFileByDate=bean;
                break;
            }
        }

        if (rechargeRecordFileByDate==null){
            rechargeRecordFileByDate = new RechargeRecordFileByDate(rechargeRecord);
            mRechargeRecordFilesByDate.add(rechargeRecordFileByDate);
        }
        return rechargeRecordFileByDate;
    }
}
