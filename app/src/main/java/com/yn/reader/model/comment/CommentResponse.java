package com.yn.reader.model.comment;

import com.yn.reader.model.BaseData;

/**
 * 评论
 * Created by sunxy on 2018/3/16.
 */

public class CommentResponse extends BaseData {
    private CommentGroup data;

    public CommentGroup getData() {
        return data;
    }

    public void setData(CommentGroup data) {
        this.data = data;
    }
}
