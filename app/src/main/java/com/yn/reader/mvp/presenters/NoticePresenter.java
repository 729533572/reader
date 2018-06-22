package com.yn.reader.mvp.presenters;

import com.hysoso.www.utillibrary.LogUtil;
import com.yn.reader.mvp.views.BaseView;
import com.yn.reader.mvp.views.NoticeView;
import com.yn.reader.network.api.MiniRest;
import com.yn.reader.model.BaseData;
import com.yn.reader.model.notice.NoticeGroup;
import com.yn.reader.util.LogUtils;

/**
 * 消息通知
 * Created by sunxy on 2018/3/14.
 */

public class NoticePresenter extends BasePresenter {
    private NoticeView noticeView;
    public NoticePresenter(NoticeView noticeView) {
        this.noticeView = noticeView;
    }

    public void getNotice(int type){
        addSubscription(MiniRest.getInstance().getNotice(type));
    }

    public void markToReadOrDelete(int type,String messageId){
        LogUtils.log("messageId="+messageId);
        addSubscription(MiniRest.getInstance().markToReadOrDelete(type,messageId));
    }
    @Override
    public BaseView getBaseView() {
        return noticeView;
    }

    @Override
    public void success(Object o) {
        if(o instanceof NoticeGroup){
            noticeView.onNoticeLoaded((NoticeGroup)o);
        }
        else if(o instanceof BaseData){
            noticeView.onNoticeMarkOrDeleted((BaseData)o);
        }
        LogUtil.e(getClass().getSimpleName(),o.toString());
    }

    @Override
    public void detachView() {
        unSubscribe();
    }
}
