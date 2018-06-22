package com.yn.reader.mvp.views;

import com.yn.reader.model.BaseData;
import com.yn.reader.model.notice.NoticeGroup;

/**
 * Created by sunxy on 2018/3/15.
 */

public interface NoticeView extends BaseView {
    void onNoticeLoaded(NoticeGroup noticeGroup);
    void onNoticeMarkOrDeleted(BaseData baseData);
}
