package com.yn.reader.mvp.views;

import com.yn.reader.model.chapter.ChapterListBean;
import com.yn.reader.model.chaptersPrice.ChaptersPrice;
import com.yn.reader.model.pay.PayResult;

import java.util.List;

/**
 * Created by luhe on 2018/3/23.
 */

public interface BuyOnLandMoreView extends BaseView{
    void onLoadChapters(List<ChapterListBean> chapterListBeans);
    void onLoadChaptersPrice(ChaptersPrice chaptersPrice);
    void paySuccess(PayResult payResult);
}
