package com.yn.reader.model.aso;

/**
 * 自主开屏广告
 * Created by sunxy on 2018/1/13.
 */

public class AsoAd {
    private int adid;
    private String adname;
    private String adtype;
    private String img_horizontal;
    private String img_vertical;
    private String link;
    private String duration;

    public int getAdid() {
        return adid;
    }

    public void setAdid(int adid) {
        this.adid = adid;
    }

    public String getAdname() {
        return adname;
    }

    public void setAdname(String adname) {
        this.adname = adname;
    }

    public String getAdtype() {
        return adtype;
    }

    public void setAdtype(String adtype) {
        this.adtype = adtype;
    }

    public String getImg_horizontal() {
        return img_horizontal;
    }

    public void setImg_horizontal(String img_horizontal) {
        this.img_horizontal = img_horizontal;
    }

    public String getImg_vertical() {
        return img_vertical;
    }

    public void setImg_vertical(String img_vertical) {
        this.img_vertical = img_vertical;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
