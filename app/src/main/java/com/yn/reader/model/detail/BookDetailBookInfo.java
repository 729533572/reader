package com.yn.reader.model.detail;

import com.yn.reader.model.common.Book;


/**
 * 书籍详情书籍信息
 * Created by sunxy on 2018/3/12.
 */

public class BookDetailBookInfo extends Book {
    private int bookwordcount;
    private int bookstatus;
    private String booklastupdate;
    private String bookdesc;
    private String bookimage;
    private BookLatestChapter booklatestchapter;
    private int booktotoalchapter;


    public static class BookLatestChapter {
        private long chapterid;
        private String chaptername;
        private int chapterrank;

        public long getChapterid() {
            return chapterid;
        }

        public void setChapterid(int chapterid) {
            this.chapterid = chapterid;
        }

        public String getChaptername() {
            return chaptername;
        }

        public void setChaptername(String chaptername) {
            this.chaptername = chaptername;
        }

        public int getChapterrank() {
            return chapterrank;
        }

        public void setChapterrank(int chapterrank) {
            this.chapterrank = chapterrank;
        }
    }

    public int getBookwordcount() {
        return bookwordcount;
    }

    public void setBookwordcount(int bookwordcount) {
        this.bookwordcount = bookwordcount;
    }

    public int getBookstatus() {
        return bookstatus;
    }

    public void setBookstatus(int bookstatus) {
        this.bookstatus = bookstatus;
    }

    public String getBooklastupdate() {
        return booklastupdate;
    }

    public void setBooklastupdate(String booklastupdate) {
        this.booklastupdate = booklastupdate;
    }

    public String getBookdesc() {
        return bookdesc;
    }

    public void setBookdesc(String bookdesc) {
        this.bookdesc = bookdesc;
    }

    @Override
    public String getBookimage() {
        return bookimage;
    }

    @Override
    public void setBookimage(String bookimage) {
        this.bookimage = bookimage;
    }

    public BookLatestChapter getBooklatestchapter() {
        return booklatestchapter;
    }

    public void setBooklatestchapter(BookLatestChapter booklatestchapter) {
        this.booklatestchapter = booklatestchapter;
    }

    public int getBooktotoalchapter() {
        return booktotoalchapter;
    }

    public void setBooktotoalchapter(int booktotoalchapter) {
        this.booktotoalchapter = booktotoalchapter;
    }


}
