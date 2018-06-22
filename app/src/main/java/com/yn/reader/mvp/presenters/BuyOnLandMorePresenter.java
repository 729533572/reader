package com.yn.reader.mvp.presenters;

import com.yn.reader.mvp.views.BaseView;
import com.yn.reader.mvp.views.BuyOnLandMoreView;
import com.yn.reader.network.api.MiniRest;
import com.yn.reader.model.chapter.ChapterGroup;
import com.yn.reader.model.chaptersPrice.ChaptersPrice;
import com.yn.reader.model.common.Book;
import com.yn.reader.model.pay.PayResult;
import com.yn.reader.util.Constant;

/**
 * Created by luhe on 2018/3/23.
 */

public class BuyOnLandMorePresenter extends BasePresenter {
    private BuyOnLandMoreView mBuyOnLandMoreView;

    public BuyOnLandMorePresenter(BuyOnLandMoreView buyOnLandMoreView) {
        mBuyOnLandMoreView = buyOnLandMoreView;
    }

    @Override
    public BaseView getBaseView() {
        return mBuyOnLandMoreView;
    }

    @Override
    public void success(Object o) {
        if (o instanceof ChapterGroup) {
            mBuyOnLandMoreView.onLoadChapters(((ChapterGroup) o).getChapters());
        } else if (o instanceof ChaptersPrice) {
            mBuyOnLandMoreView.onLoadChaptersPrice((ChaptersPrice) o);
        } else if (o instanceof PayResult) {
            mBuyOnLandMoreView.paySuccess((PayResult) o);
        }
    }

    @Override
    public void detachView() {
        unSubscribe();
    }

    public void getPrice(Book book, String chapterIds) {
        addSubscription(MiniRest.getInstance().getChaptersPrice(
                book.getBookid(),
                Constant.PAY_TYPE_CHAPTER_ID,
                book.getChapterid(),
                chapterIds,
                null));
    }

    public void payForChapters(Book book, String chapterIds) {
        addSubscription(MiniRest.getInstance().payChapter(
                book.getBookid(), book.getBookname(),
                Constant.PAY_TYPE_CHAPTER_ID, book.getChapterid(), chapterIds, null));
    }

    public void getChapters(long bookid) {
        addSubscription(MiniRest.getInstance().getChapters(bookid));
    }
}
