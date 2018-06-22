package com.yn.reader.model.hotSearch;

import android.os.Parcel;
import android.os.Parcelable;


public class HotSearch implements Parcelable {
    /*
    "id":3,
    "name":"阿旭是个大傻逼",
    "link_type":"1",
    "link_content":"链接"
    * */
    private int id;
    private String name;
    private int link_type;
    private String link_content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public int getLink_type(){
        return this.link_type;
    }
    public void setLink_type(int link_type){
        this.link_type = link_type;
    }
    public String getLink_content(){
        return this.link_content;
    }
    public void setLink_content(String link_content){
        this.link_content = link_content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.link_type);
        dest.writeString(this.link_content);
    }

    public HotSearch() {
    }

    protected HotSearch(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.link_type = in.readInt();
        this.link_content = in.readString();
    }

    public static final Creator<HotSearch> CREATOR = new Creator<HotSearch>() {
        @Override
        public HotSearch createFromParcel(Parcel source) {
            return new HotSearch(source);
        }

        @Override
        public HotSearch[] newArray(int size) {
            return new HotSearch[size];
        }
    };
}
