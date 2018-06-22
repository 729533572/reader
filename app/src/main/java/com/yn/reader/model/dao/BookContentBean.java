//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.yn.reader.model.dao;

import android.os.Parcel;
import android.os.Parcelable;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.util.ArrayList;
import java.util.List;

/**
 * 书本缓存内容
 */
@Entity
public class BookContentBean implements Parcelable{
    @Id
    private String durChapterUrl; //对应BookInfoBean noteUrl;

    private int durChapterIndex;   //当前章节  （包括番外）

    private String chapterdetails; //当前章节内容

    private String chaptertitle; //标题

    private String tag;   //来源  某个网站/本地


    @Transient
    private Boolean isRight = true;

    @Transient
    private List<String> lineContent = new ArrayList<>();

    @Transient
    private float lineSize;

    public BookContentBean(){

    }

    public float getLineSize() {
        return lineSize;
    }

    public void setLineSize(float lineSize) {
        this.lineSize = lineSize;
    }

    protected BookContentBean(Parcel in) {
        durChapterUrl = in.readString();
        durChapterIndex = in.readInt();
        chapterdetails = in.readString();
        tag = in.readString();
        lineContent = in.createStringArrayList();
        isRight = in.readByte()!=0;
    }

    @Generated(hash = 193570032)
    public BookContentBean(String durChapterUrl, int durChapterIndex, String chapterdetails,
            String chaptertitle, String tag) {
        this.durChapterUrl = durChapterUrl;
        this.durChapterIndex = durChapterIndex;
        this.chapterdetails = chapterdetails;
        this.chaptertitle = chaptertitle;
        this.tag = tag;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(durChapterUrl);
        dest.writeInt(durChapterIndex);
        dest.writeString(chapterdetails);
        dest.writeString(tag);
        dest.writeStringList(lineContent);
        dest.writeByte((byte) (isRight ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Transient
    public static final Creator<BookContentBean> CREATOR = new Creator<BookContentBean>() {
        @Override
        public BookContentBean createFromParcel(Parcel in) {
            return new BookContentBean(in);
        }

        @Override
        public BookContentBean[] newArray(int size) {
            return new BookContentBean[size];
        }
    };

    public String getDurChapterUrl() {
        return durChapterUrl;
    }

    public void setDurChapterUrl(String durChapterUrl) {
        this.durChapterUrl = durChapterUrl;
    }

    public int getDurChapterIndex() {
        return durChapterIndex;
    }

    public void setDurChapterIndex(int durChapterIndex) {
        this.durChapterIndex = durChapterIndex;
    }

    public String getChapterdetails() {
        return chapterdetails;
    }

    public void setChapterdetails(String chapterdetails) {
        this.chapterdetails = chapterdetails;
        if(chapterdetails ==null || chapterdetails.length()==0)
            this.isRight = false;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<String> getLineContent() {
        return lineContent;
    }

    public void setLineContent(List<String> lineContent) {
        this.lineContent = lineContent;
    }

    public Boolean getRight() {
        return isRight;
    }

    public void setRight(Boolean right) {
        isRight = right;
    }

    public void setChaptertitle(String chaptertitle) {
        this.chaptertitle = chaptertitle;
    }

    public String getChaptertitle() {
        return chaptertitle;
    }
}
