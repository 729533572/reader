package com.yn.reader.mvp.views;

import com.yn.reader.model.pay.PayResult;

/**
 * Created by luhe on 2018/3/26.
 */

public interface BuyPopOnLandView extends BaseView{
    void paySuccess(PayResult result);
}
