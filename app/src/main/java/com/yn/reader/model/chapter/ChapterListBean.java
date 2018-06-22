//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.yn.reader.model.chapter;

import android.os.Parcel;
import android.os.Parcelable;

import com.yn.reader.model.dao.BookContentBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 章节列表
 */
@Entity
public class ChapterListBean implements Parcelable, Cloneable {
    /*
    *  "chapterid":"1",
        "chapterrank":"1",
        "chaptername":"楔子 张天师祈禳瘟疫 洪太尉误走妖魔",
        "chapterprice":"0",章节价格
        "chapterwordcount":"6522",章节字数统计
        "chaptershoptype":1,用户是否已购买
        "progress":"1",章节进度
    * */

    private String noteUrl; //对应BookInfoBean noteUrl;

    private int chapterrank;  //当前章节数
    @Id
    private long chapterid;  //当前章节对应的文章地址

    private String chaptername;  //当前章节名称

    private String tag;

    private Boolean hasCache = false;

    private float chapterprice;

    private long chapterwordcount;

    private int chaptershoptype;//用户是否已购买

    private float progress;

    private long bookId;

    @Transient
    private BookContentBean bookContentBean;

    protected ChapterListBean(Parcel in) {
        noteUrl = in.readString();
        chapterrank = in.readInt();
        chapterid = in.readLong();
        chaptername = in.readString();
        chapterprice = in.readFloat();
        chapterwordcount = in.readLong();
        chaptershoptype = in.readInt();
        progress = in.readFloat();
        tag = in.readString();
        bookContentBean = in.readParcelable(BookContentBean.class.getClassLoader());
        hasCache = in.readByte() != 0;
    }


    @Generated(hash = 379348179)
    public ChapterListBean(String noteUrl, int chapterrank, long chapterid,
            String chaptername, String tag, Boolean hasCache, float chapterprice,
            long chapterwordcount, int chaptershoptype, float progress, long bookId) {
        this.noteUrl = noteUrl;
        this.chapterrank = chapterrank;
        this.chapterid = chapterid;
        this.chaptername = chaptername;
        this.tag = tag;
        this.hasCache = hasCache;
        this.chapterprice = chapterprice;
        this.chapterwordcount = chapterwordcount;
        this.chaptershoptype = chaptershoptype;
        this.progress = progress;
        this.bookId = bookId;
    }


    @Generated(hash = 1096893365)
    public ChapterListBean() {
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(noteUrl);
        dest.writeInt(chapterrank);
        dest.writeLong(chapterid);
        dest.writeString(chaptername);
        dest.writeFloat(chapterprice);
        dest.writeLong(chapterwordcount);
        dest.writeInt(chaptershoptype);
        dest.writeFloat(progress);
        dest.writeString(tag);
        dest.writeParcelable(bookContentBean, flags);
        dest.writeByte((byte) (hasCache ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public BookContentBean getBookContentBean() {
        return bookContentBean;
    }

    public void setBookContentBean(BookContentBean bookContentBean) {
        this.bookContentBean = bookContentBean;
    }

    public Boolean getHasCache() {
        return this.hasCache;
    }

    public void setHasCache(Boolean hasCache) {
        this.hasCache = hasCache;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getChaptername() {
        return this.chaptername;
    }

    public void setChaptername(String chaptername) {
        this.chaptername = chaptername;
    }

    public long getChapterid() {
        return this.chapterid;
    }

    public void setChapterid(long chapterid) {
        this.chapterid = chapterid;
    }

    public int getChapterrank() {
        return this.chapterrank;
    }

    public void setChapterrank(int chapterrank) {
        this.chapterrank = chapterrank;
    }

    public String getNoteUrl() {
        return this.noteUrl;
    }

    public void setNoteUrl(String noteUrl) {
        this.noteUrl = noteUrl;
    }

    @Transient
    public static final Creator<ChapterListBean> CREATOR = new Creator<ChapterListBean>() {
        @Override
        public ChapterListBean createFromParcel(Parcel in) {
            return new ChapterListBean(in);
        }

        @Override
        public ChapterListBean[] newArray(int size) {
            return new ChapterListBean[size];
        }
    };

    @Override
    public Object clone() throws CloneNotSupportedException {
        ChapterListBean chapterListBean = (ChapterListBean) super.clone();
        chapterListBean.noteUrl = noteUrl;
        chapterListBean.chapterid = chapterid;
        chapterListBean.chaptername = chaptername;
        chapterListBean.chapterprice = chapterprice;
        chapterListBean.chapterwordcount = chapterwordcount;
        chapterListBean.chaptershoptype = chaptershoptype;
        chapterListBean.progress = progress;
        chapterListBean.tag = tag;
        chapterListBean.hasCache = hasCache;
        chapterListBean.bookContentBean = new BookContentBean();
        return chapterListBean;
    }

    public void setChapterprice(float chapterprice) {
        this.chapterprice = chapterprice;
    }

    public float getChapterprice() {
        return chapterprice;
    }

    public void setChapterwordcount(long chapterwordcount) {
        this.chapterwordcount = chapterwordcount;
    }

    public long getChapterwordcount() {
        return chapterwordcount;
    }

    public void setChaptershoptype(int chaptershoptype) {
        this.chaptershoptype = chaptershoptype;
    }

    public int getChaptershoptype() {
        return chaptershoptype;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public float getProgress() {
        return progress;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public long getBookId() {
        return bookId;
    }
}
