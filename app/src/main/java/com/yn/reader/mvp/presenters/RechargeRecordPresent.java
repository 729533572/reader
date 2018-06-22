package com.yn.reader.mvp.presenters;

import com.yn.reader.mvp.views.BaseView;
import com.yn.reader.mvp.views.RechargeRecordView;
import com.yn.reader.network.api.MiniRest;
import com.yn.reader.model.rechargeRecord.RechargeRecordGroup;

/**
 * Created by luhe on 2018/3/21.
 */

public class RechargeRecordPresent extends BasePresenter{
    private RechargeRecordView mRechargeRecordView;
    public RechargeRecordPresent(RechargeRecordView rechargeRecordView){
        this.mRechargeRecordView = rechargeRecordView;
    }
    @Override
    public BaseView getBaseView() {
        return mRechargeRecordView;
    }

    @Override
    public void success(Object o) {
        if (o instanceof RechargeRecordGroup){
            mRechargeRecordView.onLoadRechargeRecord(((RechargeRecordGroup) o).getPaylist());
        }
    }

    @Override
    public void detachView() {
        unSubscribe();
    }

    public void getRechargeRecord() {
        addSubscription(MiniRest.getInstance().getRechargeRecord());
    }
}
