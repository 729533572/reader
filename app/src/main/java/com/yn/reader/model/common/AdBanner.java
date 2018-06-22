package com.yn.reader.model.common;

/**
 * 通用广告banner
 * Created by sunxy on 2018/3/12.
 */

public class AdBanner {
    private boolean isshow;
    private int adtype;
    private String image;
    private String link_content;
    private String link_type;
    private String name;
    private String adid;
    private String source;

    public boolean isIsshow() {
        return isshow;
    }

    public void setIsshow(boolean isshow) {
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

    public String getAdid() {
        return adid;
    }

    public void setAdid(String adid) {
        this.adid = adid;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
