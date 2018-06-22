package com.yn.reader.mvp.presenters;

import com.hysoso.www.utillibrary.ToastUtil;
import com.yn.reader.R;
import com.yn.reader.mvp.views.BaseView;
import com.yn.reader.mvp.views.CommentHotMoreView;
import com.yn.reader.network.api.MiniRest;
import com.yn.reader.model.comment.CommentResponse;
import com.yn.reader.model.comment.LikeComment;
import com.yn.reader.model.comment.ReportComment;
import com.yn.reader.util.Constant;

/**
 * Created by luhe on 2018/3/20.
 */

public class CommentHotMorePresenter extends BasePresenter {
    private CommentHotMoreView mCommentHotMoreView;
    private int currentHotPage = 1;

    public CommentHotMorePresenter(CommentHotMoreView commentHotMoreView) {
        mCommentHotMoreView = commentHotMoreView;
    }

    @Override
    public BaseView getBaseView() {
        return mCommentHotMoreView;
    }

    @Override
    public void success(Object o) {
        if (o instanceof CommentResponse) {
            mCommentHotMoreView.onCommentsLoaded(((CommentResponse) o).getData());
        } else if (o instanceof LikeComment) {
            //TODO:
        } else if (o instanceof ReportComment) {
            ToastUtil.showShort(mCommentHotMoreView.getContext(),
                    mCommentHotMoreView.getContext().getString(R.string.tip_comment_report_success));
        }
    }

    @Override
    public void detachView() {
        unSubscribe();
    }

    public void getHotComments(long bookId, int pageSize) {
        addSubscription(MiniRest.getInstance().getComments(bookId, currentHotPage, Constant.SORT_TYPE_HOT, pageSize));
        ++currentHotPage;
    }

    public void like(int commentid) {
        addSubscription(MiniRest.getInstance().likeComment(commentid));
    }

    public void report(int commentid) {
        addSubscription(MiniRest.getInstance().report(commentid));
    }
}
