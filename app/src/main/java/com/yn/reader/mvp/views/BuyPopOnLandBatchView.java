package com.yn.reader.mvp.views;

import com.yn.reader.model.chaptersPrice.ChaptersPrice;
import com.yn.reader.model.pay.PayResult;

/**
 * Created by luhe on 2018/3/26.
 */

public interface BuyPopOnLandBatchView extends BaseView{
    void onLoadChaptersPrice(ChaptersPrice chaptersPrice);
    void paySuccess(PayResult payResult);
}
