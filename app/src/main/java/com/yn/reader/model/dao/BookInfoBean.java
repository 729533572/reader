//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.yn.reader.model.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.yn.reader.model.chapter.ChapterListBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 书本信息
 */
@Entity
public class BookInfoBean implements Parcelable,Cloneable {
    @Transient
    public static final long REFRESH_DUR = 10*60*1000;
    private String chapterUrl;  //章节目录地址
    @Transient
    private List<ChapterListBean> chapterlist = new ArrayList<>();    //章节列表
    private long finalRefreshData;  //章节最后更新时间

    public BookInfoBean(){

    }

    protected BookInfoBean(Parcel in) {
        chapterUrl = in.readString();
        chapterlist = in.createTypedArrayList(ChapterListBean.CREATOR);
        finalRefreshData = in.readLong();
    }

    @Generated(hash = 721754510)
    public BookInfoBean(String chapterUrl, long finalRefreshData) {
        this.chapterUrl = chapterUrl;
        this.finalRefreshData = finalRefreshData;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(chapterUrl);
        dest.writeTypedList(chapterlist);
        dest.writeLong(finalRefreshData);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Transient
    public static final Creator<BookInfoBean> CREATOR = new Creator<BookInfoBean>() {
        @Override
        public BookInfoBean createFromParcel(Parcel in) {
            return new BookInfoBean(in);
        }

        @Override
        public BookInfoBean[] newArray(int size) {
            return new BookInfoBean[size];
        }
    };


    public String getChapterUrl() {
        return chapterUrl;
    }

    public void setChapterUrl(String chapterUrl) {
        this.chapterUrl = chapterUrl;
    }

    public List<ChapterListBean> getChapterlist() {
        return chapterlist;
    }

    public void setChapterlist(List<ChapterListBean> chapterlist) {
        this.chapterlist = chapterlist;
    }
    public void addChapterlist(List<ChapterListBean> chapterlist){
        this.chapterlist.addAll(chapterlist);
    }

    public long getFinalRefreshData() {
        return finalRefreshData;
    }

    public void setFinalRefreshData(long finalRefreshData) {
        this.finalRefreshData = finalRefreshData;
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        BookInfoBean bookInfoBean = (BookInfoBean) super.clone();
        bookInfoBean.chapterUrl = chapterUrl;
        if(chapterlist!=null){
            List<ChapterListBean> newList = new ArrayList<>();
            Iterator<ChapterListBean> iterator = chapterlist.iterator();
            while(iterator.hasNext()){
                newList.add((ChapterListBean) iterator.next().clone());
            }
            bookInfoBean.setChapterlist(newList);
        }
        return bookInfoBean;
    }
}