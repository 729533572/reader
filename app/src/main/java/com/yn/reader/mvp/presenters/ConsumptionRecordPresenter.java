package com.yn.reader.mvp.presenters;

import com.yn.reader.mvp.views.BaseView;
import com.yn.reader.mvp.views.ConsumptionRecordView;
import com.yn.reader.network.api.MiniRest;
import com.yn.reader.model.consumptionRecord.ConsumptionRecordGroup;

/**
 * Created by luhe on 2018/3/21.
 */

public class ConsumptionRecordPresenter extends BasePresenter{
    private ConsumptionRecordView mConsumptionRecordView;
    public ConsumptionRecordPresenter(ConsumptionRecordView consumptionRecordView){
        mConsumptionRecordView = consumptionRecordView;
    }
    @Override
    public BaseView getBaseView() {
        return mConsumptionRecordView;
    }

    @Override
    public void success(Object o) {
        if (o instanceof ConsumptionRecordGroup){
            mConsumptionRecordView.onLoadConsumptionRecords(((ConsumptionRecordGroup) o).getShoplist());
        }
    }

    @Override
    public void detachView() {
        unSubscribe();
    }

    public void getConsumptionRecord() {
        addSubscription(MiniRest.getInstance().getConsumptionRecord());
    }
}
