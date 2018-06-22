package com.yn.reader.model.consumptionRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luhe on 2018/3/21.
 */

public class ConsumptionRecordFileByDateManager {
    private static ConsumptionRecordFileByDateManager mSingleInstance;
    private List<ConsumptionRecordFileByDate> ConsumptionRecordFilesByDate;
    private ConsumptionRecordFileByDateManager(){}

    public static ConsumptionRecordFileByDateManager getInstance() {
        if (mSingleInstance==null){
         synchronized (ConsumptionRecordFileByDateManager.class){
             if (mSingleInstance==null)mSingleInstance = new ConsumptionRecordFileByDateManager();
         }
        }
        return mSingleInstance;
    }
    public List<ConsumptionRecordFileByDate> getConsumptionRecordFilesByDate(List<ConsumptionRecord> consumptionRecords){
        ConsumptionRecordFilesByDate = new ArrayList<>();

        for (ConsumptionRecord bean:consumptionRecords) {
            ConsumptionRecordFileByDate file = getFileByConsumptionRecord(bean);
            file.addRechargeRecord(bean);
        }

        return ConsumptionRecordFilesByDate;
    }
    private ConsumptionRecordFileByDate getFileByConsumptionRecord(ConsumptionRecord consumptionRecord){
        ConsumptionRecordFileByDate consumptionRecordFileByDate = null;

        for (ConsumptionRecordFileByDate bean:ConsumptionRecordFilesByDate) {
            if (consumptionRecord.getCreatedate().contains(bean.getDate())){
                consumptionRecordFileByDate=bean;
                break;
            }
        }

        if (consumptionRecordFileByDate==null){
            consumptionRecordFileByDate = new ConsumptionRecordFileByDate(consumptionRecord);
            ConsumptionRecordFilesByDate.add(consumptionRecordFileByDate);
        }
        return consumptionRecordFileByDate;
    }
}
