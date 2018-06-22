package com.yn.reader.mvp.views;

import com.yn.reader.model.comment.CommentGroup;

/**
 * Created by luhe on 2018/3/20.
 */

public interface CommentHotMoreView extends BaseView{
    void onCommentsLoaded(CommentGroup data);
}
