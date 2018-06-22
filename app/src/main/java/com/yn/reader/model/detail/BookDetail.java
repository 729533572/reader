package com.yn.reader.model.detail;

/**
 * 书籍详情
 * Created by sunxy on 2018/3/12.
 */

public class BookDetail {
    private BookDetailBookInfo book;
    private BookDetailAd ad;

    private Currentprogress currentprogress;

    public BookDetailBookInfo getBook() {
        return book;
    }

    public void setBook(BookDetailBookInfo book) {
        this.book = book;
    }

    public BookDetailAd getAd() {
        return ad;
    }

    public void setAd(BookDetailAd ad) {
        this.ad = ad;
    }

    public Currentprogress getCurrentprogress() {
        return currentprogress;
    }

    public void setCurrentprogress(Currentprogress currentprogress) {
        this.currentprogress = currentprogress;
    }

    public static class Currentprogress {
        private int chapterid;
        private String progress;

        public int getChapterid() {
            return chapterid;
        }

        public void setChapterid(int chapterid) {
            this.chapterid = chapterid;
        }

        public String getProgress() {
            return progress;
        }

        public void setProgress(String progress) {
            this.progress = progress;
        }
    }
}
