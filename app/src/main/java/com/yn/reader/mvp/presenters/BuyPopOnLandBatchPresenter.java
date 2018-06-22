package com.yn.reader.mvp.presenters;

import com.yn.reader.mvp.views.BaseView;
import com.yn.reader.mvp.views.BuyPopOnLandBatchView;
import com.yn.reader.network.api.MiniRest;
import com.yn.reader.model.chaptersPrice.ChaptersPrice;
import com.yn.reader.model.common.Book;
import com.yn.reader.model.pay.PayResult;
import com.yn.reader.util.Constant;

/**
 * Created by luhe on 2018/3/26.
 */

public class BuyPopOnLandBatchPresenter extends BasePresenter {
    private BuyPopOnLandBatchView mBuyPopOnLandBatchView;

    public BuyPopOnLandBatchPresenter(BuyPopOnLandBatchView buyPopOnLandBatchView) {
        mBuyPopOnLandBatchView = buyPopOnLandBatchView;
    }

    @Override
    public BaseView getBaseView() {
        return mBuyPopOnLandBatchView;
    }

    @Override
    public void success(Object o) {
        if (o instanceof ChaptersPrice) {
            mBuyPopOnLandBatchView.onLoadChaptersPrice((ChaptersPrice) o);
        } else if (o instanceof PayResult) {
            mBuyPopOnLandBatchView.paySuccess((PayResult) o);
        }
    }

    @Override
    public void detachView() {
        unSubscribe();
    }

    public void getPrice(Book book, long chapterIdBought, int chapterCount) {
        addSubscription(MiniRest.getInstance().getChaptersPrice(
                book.getBookid(),
                Constant.PAY_TYPE_CHAPTER_COUNT,
                chapterIdBought,
                null,
                chapterCount));
    }

    public void payForChapters(Book book, long chapterIdBought, int chapterCount) {
        addSubscription(MiniRest.getInstance().payChapter(
                book.getBookid(), book.getBookname(),
                Constant.PAY_TYPE_CHAPTER_COUNT,chapterIdBought, null, chapterCount));
    }
}
