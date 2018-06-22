package com.yn.reader.mvp.presenters;

import com.yn.reader.mvp.views.BaseView;
import com.yn.reader.mvp.views.BuyPopOnLandView;
import com.yn.reader.network.api.MiniRest;
import com.yn.reader.model.common.Book;
import com.yn.reader.model.pay.PayResult;
import com.yn.reader.util.Constant;

/**
 * Created by luhe on 2018/3/26.
 */

public class BuyPopOnLandPresenter extends BasePresenter{
    private BuyPopOnLandView mBuyPopOnLandView;

    public BuyPopOnLandPresenter(BuyPopOnLandView buyPopOnLandView){
        mBuyPopOnLandView = buyPopOnLandView;
    }
    @Override
    public BaseView getBaseView() {
        return mBuyPopOnLandView;
    }

    @Override
    public void success(Object o) {
        if (o instanceof PayResult){
            mBuyPopOnLandView.paySuccess((PayResult)o);
        }
    }

    @Override
    public void detachView() {
        unSubscribe();
    }

    public void payForChapter(Book book,long chapterId) {
        addSubscription(MiniRest.getInstance().payChapter(
                book.getBookid(),book.getBookname(),
                Constant.PAY_TYPE_CHAPTER_ID,chapterId,chapterId+"",null));
    }
}
