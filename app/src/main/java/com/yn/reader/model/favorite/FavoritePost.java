package com.yn.reader.model.favorite;

/**
 * 添加收藏
 * <p>
 * Created by sunxy on 2018/3/22.
 */

public class FavoritePost {
    private long userid;
    private Long bookid;
    private long chapterid;
    private String progress;
    private String updatedate;

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public Long getBookid() {
        return bookid;
    }

    public void setBookid(long bookid) {
        this.bookid = bookid;
    }

    public long getChapterid() {
        return chapterid;
    }

    public void setChapterid(long chapterid) {
        this.chapterid = chapterid;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public void setUpdatedate(String updatedate) {
        this.updatedate = updatedate;
    }

    public String getUpdatedate() {
        return updatedate;
    }
}
