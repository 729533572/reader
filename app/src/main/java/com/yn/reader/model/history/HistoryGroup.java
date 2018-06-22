package com.yn.reader.model.history;

import com.yn.reader.model.BaseData;
import com.yn.reader.model.common.Book;

import java.util.List;

/**
 * 书架
 * Created by sunxy on 2018/3/12.
 */

public class HistoryGroup extends BaseData {
    private int total;
    private int pagesize;
    private int maxpageno;
    private int pageno;
    private List<Book> data;

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

    public int getMaxpageno() {
        return maxpageno;
    }

    public void setMaxpageno(int maxpageno) {
        this.maxpageno = maxpageno;
    }

    public int getPageno() {
        return pageno;
    }

    public void setPageno(int pageno) {
        this.pageno = pageno;
    }

    public List<Book> getData() {
        return data;
    }

    public void setData(List<Book> data) {
        this.data = data;
    }
}
