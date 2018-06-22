package com.yn.reader.mvp.views;

import com.yn.reader.model.buy.BuyGroup;
import com.yn.reader.model.pay.PayRequire;

/**
 * Created by luhe on 2018/3/22.
 */

public interface BuyView extends BaseView{
    void onLoadBuyChoices(BuyGroup o);
    void onLoadPayRequire(PayRequire payRequire);
}
