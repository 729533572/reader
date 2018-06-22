package com.yn.reader.model.booklist;

import com.yn.reader.model.BaseData;

/**
 * 书籍列表
 * Created by sunxy on 2018/3/16.
 */

public class BookListResponse extends BaseData {
    private int total;
    private int pagesize;
    private int pageno;
    private int maxpageno;
    private BookListGroup data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getPageno() {
        return pageno;
    }

    public void setPageno(int pageno) {
        this.pageno = pageno;
    }

    public int getMaxpageno() {
        return maxpageno;
    }

    public void setMaxpageno(int maxpageno) {
        this.maxpageno = maxpageno;
    }

    public BookListGroup getData() {
        return data;
    }

    public void setData(BookListGroup data) {
        this.data = data;
    }
}
