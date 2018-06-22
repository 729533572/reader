package com.yn.reader.model.detail;

import com.yn.reader.model.BaseData;

/**
 * 书籍详情
 * Created by sunxy on 2018/3/12.
 */

public class BookDetailGroup extends BaseData {
    private BookDetail data;

    public BookDetail getData() {
        return data;
    }

    public void setData(BookDetail data) {
        this.data = data;
    }
}
