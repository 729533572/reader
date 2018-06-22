package com.yn.reader.model.home;

import com.yn.reader.MiniApp;
import com.yn.reader.R;
import com.yn.reader.model.common.AdBanner;
import com.yn.reader.model.common.Book;
import com.yn.reader.util.Constant;

import java.util.List;

/**
 * 首页书籍分类合集
 * Created by sunxy on 2018/3/12.
 */

public class Channel {
    public static int PRE_START_INDEX = 0;
    private int home_channel_id;
    private String home_channel_name;
    private String home_channel_type_text;
    private int home_channel_type;
    private AdBanner ad;
    private List<Book> books;
    private int currentIndex = PRE_START_INDEX;//用于换一换
    private int type;

    public AdBanner getAd() {
        return ad;
    }

    public void setAd(AdBanner ad) {
        this.ad = ad;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public int getHome_channel_id() {
        return home_channel_id;
    }

    public void setHome_channel_id(int home_channel_id) {
        this.home_channel_id = home_channel_id;
    }

    public String getHome_channel_name() {
        return home_channel_name;
    }

    public void setHome_channel_name(String home_channel_name) {
        this.home_channel_name = home_channel_name;

    }

    public int getHome_channel_type() {
        return home_channel_type;
    }

    public void setHome_channel_type(int home_channel_type) {
        this.home_channel_type = home_channel_type;
        switch (home_channel_type) {
            case Constant.CHANNEL_TYPE_CHANGE_BATCH:
                this.home_channel_type_text = MiniApp.getInstance().getString(R.string.change_other);
                break;
            case Constant.CHANNEL_TYPE_CHANGE_MORE:
                this.home_channel_type_text = MiniApp.getInstance().getString(R.string.look_more);
                break;
            case Constant.CHANNEL_TYPE_CHANGE_HOT:
                this.home_channel_type_text = MiniApp.getInstance().getString(R.string.hot);
                break;
        }
    }

    public String getHome_channel_type_text() {
        return home_channel_type_text;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
