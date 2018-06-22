//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.yn.reader.model.dao;

import java.util.List;

public class ReadBookContentBean {
    private List<BookContentBean> bookContentList;
    private int pageIndex;

    public ReadBookContentBean(List<BookContentBean> bookContentList, int pageIndex){
        this.bookContentList =  bookContentList;
        this.pageIndex = pageIndex;
    }

    public List<BookContentBean> getBookContentList() {
        return bookContentList;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }
}
