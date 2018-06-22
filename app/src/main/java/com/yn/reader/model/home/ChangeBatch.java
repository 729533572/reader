package com.yn.reader.model.home;

import com.yn.reader.model.common.Book;

import java.util.List;

/**
 * 换一批
 * Created by sunxy on 2018/3/12.
 */

public class ChangeBatch {
    private int homechannelid;
    private List<Book>books;

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public int getHomechannelid() {
        return homechannelid;
    }

    public void setHomechannelid(int homechannelid) {
        this.homechannelid = homechannelid;
    }
}
