package com.yn.reader.model.jpush;

import android.os.Parcel;
import android.os.Parcelable;

/**推送自定义消息
 * Created by sunxy on 2018/1/16.
 */
//{"type":"newsdetail","link":"http:\/\/ba.crazyzha.com\/h5\/detail?messageid=b5706b07df376579"}
public class JPushCustomMsg implements Parcelable {

    private String type;
    private String link;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.link);
    }

    public JPushCustomMsg() {
    }

    protected JPushCustomMsg(Parcel in) {
        this.type = in.readString();
        this.link = in.readString();
    }

    public static final Creator<JPushCustomMsg> CREATOR = new Creator<JPushCustomMsg>() {
        @Override
        public JPushCustomMsg createFromParcel(Parcel source) {
            return new JPushCustomMsg(source);
        }

        @Override
        public JPushCustomMsg[] newArray(int size) {
            return new JPushCustomMsg[size];
        }
    };
}
