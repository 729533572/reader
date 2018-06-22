package com.yn.reader.model.searchResult;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 消息中心
 * Created by sunxy on 2018/3/6.
 */

public class SearchResultBook implements Parcelable {
    /*
    "bookid":"1",
    "bookname":"金瓶梅",
    "bookauthor":"王成龙",
    "bookimage":"http://114.55.232.1/product/web/image/novel/books/cover/cover.png",
    "bookdesc":"成龙最爱的小说系列，讲述的是一个凄美的爱情故事。",
    "booktags":[
        "色情",
        "暴力",
        "武侠",
        "言情"
    ]
    * */
    private int bookid;
    private String bookname;
    private String bookauthor;
    private String bookimage;
    private String bookdesc;
    private String[] booktags;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.bookid);
        dest.writeString(this.bookname);
        dest.writeString(this.bookauthor);
        dest.writeString(this.bookimage);
        dest.writeString(this.bookdesc);
        dest.writeStringArray(this.booktags);
    }

    public SearchResultBook() {
    }

    protected SearchResultBook(Parcel in) {
        this.bookid = in.readInt();
        this.bookname = in.readString();
        this.bookauthor = in.readString();
        this.bookimage = in.readString();
        this.bookdesc = in.readString();
        in.readStringArray(this.getBooktags());
    }

    public static final Creator<SearchResultBook> CREATOR = new Creator<SearchResultBook>() {
        @Override
        public SearchResultBook createFromParcel(Parcel source) {
            return new SearchResultBook(source);
        }

        @Override
        public SearchResultBook[] newArray(int size) {
            return new SearchResultBook[size];
        }
    };

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    public int getBookid() {
        return bookid;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookauthor(String bookauthor) {
        this.bookauthor = bookauthor;
    }

    public String getBookauthor() {
        return bookauthor;
    }

    public void setBookimage(String bookimage) {
        this.bookimage = bookimage;
    }

    public String getBookimage() {
        return bookimage;
    }

    public void setBookdesc(String bookdesc) {
        this.bookdesc = bookdesc;
    }

    public String getBookdesc() {
        return bookdesc;
    }

    public void setBooktags(String[] booktags) {
        this.booktags = booktags;
    }

    public String[] getBooktags() {
        return booktags;
    }
}
