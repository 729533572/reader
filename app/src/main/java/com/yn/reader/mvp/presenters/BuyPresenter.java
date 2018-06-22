package com.yn.reader.mvp.presenters;

import com.yn.reader.mvp.views.BaseView;
import com.yn.reader.mvp.views.BuyView;
import com.yn.reader.network.api.MiniRest;
import com.yn.reader.model.buy.BuyGroup;
import com.yn.reader.model.pay.PayRequire;
import com.yn.reader.util.Constant;

/**
 * Created by luhe on 2018/3/22.
 */

public class BuyPresenter extends BasePresenter {
    private BuyView mBuyView;

    public BuyPresenter(BuyView buyView) {
        mBuyView = buyView;
    }

    @Override
    public BaseView getBaseView() {
        return mBuyView;
    }

    @Override
    public void success(Object o) {
        if (o instanceof BuyGroup) {
            mBuyView.onLoadBuyChoices((BuyGroup) o);
        } else if (o instanceof PayRequire) {
            mBuyView.onLoadPayRequire((PayRequire) o);
        }
    }

    @Override
    public void detachView() {
        unSubscribe();
    }

    public void getBuyChoices() {
        addSubscription(
                MiniRest.getInstance().getBuyChoices(mBuyView.getContext().getIntent().getIntExtra(Constant.KEY_TYPE, Constant.BUY_TYPE_MONTHLY))
        );
    }

    public void pay() {
        addSubscription(MiniRest.getInstance().pay());
    }

}
