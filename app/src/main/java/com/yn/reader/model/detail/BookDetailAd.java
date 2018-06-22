package com.yn.reader.model.detail;

/**
 * 书籍详情广告
 * Created by sunxy on 2018/3/12.
 */

public class BookDetailAd {
    private int isshow;
    private int adtype;
    private String image;
    private String link_content;
    private String link_type;
    private String name;
    private int adid;
    private String source;

    public int getIsshow() {
        return isshow;
    }

    public void setIsshow(int isshow) {
        this.isshow = isshow;
    }

    public int getAdtype() {
        return adtype;
    }

    public void setAdtype(int adtype) {
        this.adtype = adtype;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink_content() {
        return link_content;
    }

    public void setLink_content(String link_content) {
        this.link_content = link_content;
    }

    public String getLink_type() {
        return link_type;
    }

    public void setLink_type(String link_type) {
        this.link_type = link_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAdid() {
        return adid;
    }

    public void setAdid(int adid) {
        this.adid = adid;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
