package com.yn.reader.model.booklist;

import com.yn.reader.model.common.Book;

import java.util.List;

/**
 * 书籍列表
 * Created by sunxy on 2018/3/16.
 */

public class BookListGroup{
    private List<Book>heatrank;

    public List<Book> getHeatrank() {
        return heatrank;
    }

    public void setHeatrank(List<Book> heatrank) {
        this.heatrank = heatrank;
    }
}
