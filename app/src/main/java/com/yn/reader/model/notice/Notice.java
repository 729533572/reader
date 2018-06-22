package com.yn.reader.model.notice;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 消息中心
 * Created by sunxy on 2018/3/6.
 */

public class
Notice implements Parcelable {
    private int id;
    private String title;
    private String content;
    private String createdate;
    private int bookid;
    private int status;//0，未读；1，已读

    private boolean isSelect;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.createdate);
        dest.writeInt(this.bookid);
        dest.writeInt(this.status);
        dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
    }

    public Notice() {
    }

    protected Notice(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.content = in.readString();
        this.createdate = in.readString();
        this.bookid = in.readInt();
        this.status = in.readInt();
        this.isSelect = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Notice> CREATOR = new Parcelable.Creator<Notice>() {
        @Override
        public Notice createFromParcel(Parcel source) {
            return new Notice(source);
        }

        @Override
        public Notice[] newArray(int size) {
            return new Notice[size];
        }
    };
}
