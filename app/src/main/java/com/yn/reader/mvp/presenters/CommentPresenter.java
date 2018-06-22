package com.yn.reader.mvp.presenters;

import com.hysoso.www.utillibrary.ToastUtil;
import com.yn.reader.R;
import com.yn.reader.mvp.views.BaseView;
import com.yn.reader.mvp.views.CommentView;
import com.yn.reader.network.api.MiniRest;
import com.yn.reader.model.BaseData;
import com.yn.reader.model.comment.CommentResponse;
import com.yn.reader.model.comment.LikeComment;
import com.yn.reader.model.comment.ReportComment;
import com.yn.reader.util.Constant;

/**
 * Created by luhe on 2018/3/20.
 */

public class CommentPresenter extends BasePresenter {
    private CommentView mCommentView;
    private int currentHotPage = 1;
    private int currentLastPage = 1;

    public CommentPresenter(CommentView commentView) {
        mCommentView = commentView;
    }

    public void getHotComments(long bookId, int pageSize) {
        addSubscription(MiniRest.getInstance().getComments(bookId, currentHotPage, Constant.SORT_TYPE_HOT, pageSize));
    }

    public void getLastComments(long bookId, int pageSize) {
        addSubscription(MiniRest.getInstance().getComments(bookId, currentLastPage, Constant.SORT_TYPE_TIME, pageSize));
    }

    @Override
    public BaseView getBaseView() {
        return mCommentView;
    }

    @Override
    public void success(Object o) {
        if (o instanceof CommentResponse) {
            mCommentView.onCommentsLoaded(((CommentResponse) o).getData());
        } else if (o instanceof LikeComment) {
            //TODO:
        } else if (o instanceof ReportComment) {
            ToastUtil.showShort(mCommentView.getContext(),
                    mCommentView.getContext().getString(R.string.tip_comment_report_success));
        } else if (o instanceof BaseData) {
            mCommentView.hideKeyBoardAndLoading();
            mCommentView.enableSendBookComment();
            mCommentView.cleanInputContent();
            mCommentView.updateLastComment();
        }
    }

    @Override
    public void onFailure(int code, String msg) {
        mCommentView.hideKeyBoardAndLoading();
        mCommentView.enableSendBookComment();
        super.onFailure(code, msg);
    }

    @Override
    public void detachView() {
        unSubscribe();
    }

    public void sendBookComment(long bookId, String content) {
        addSubscription(MiniRest.getInstance().sendBookComment(bookId, content));
    }

    public void like(int commentid) {
        addSubscription(MiniRest.getInstance().likeComment(commentid));
    }

    public void report(int commentid) {
        addSubscription(MiniRest.getInstance().report(commentid));
    }
}
