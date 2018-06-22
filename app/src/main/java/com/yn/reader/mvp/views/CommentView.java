package com.yn.reader.mvp.views;

import com.yn.reader.model.comment.CommentGroup;

/**
 * Created by luhe on 2018/3/20.
 */

public interface CommentView extends BaseView{
    void onCommentsLoaded(CommentGroup data);

    void updateLastComment();

    void hideKeyBoardAndLoading();
    void cleanInputContent();

    void enableSendBookComment();
}
