package com.yn.reader.model.buy;

/**
 * Created by luhe on 2018/3/22.
 */

public class BuyAdvertisement {
    /*
    * "isshow":1,
        "adtype":1,
        "image":"http://114.55.232.1/jpush/frontend/web/image/browser/mainconfig/default/ad/20171113161024.jpeg",
        "link_content":"1",
        "link_type":"book",
        "name":"广告三书广告",
        "adid":3,
        "source":"shopvipitemad"
    * */
    private int isshow;
    private int adtype;//1,图片广告；2，文字广告
    private String image;
    private String link_content;
    private String link_type;
    private String name;
    private Long adid;
    private String source;

    public void setIsshow(int isshow) {
        this.isshow = isshow;
    }

    public int getIsshow() {
        return isshow;
    }

    public void setAdtype(int adtype) {
        this.adtype = adtype;
    }

    public int getAdtype() {
        return adtype;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setLink_content(String link_content) {
        this.link_content = link_content;
    }

    public String getLink_content() {
        return link_content;
    }

    public void setLink_type(String link_type) {
        this.link_type = link_type;
    }

    public String getLink_type() {
        return link_type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAdid(Long adid) {
        this.adid = adid;
    }

    public Long getAdid() {
        return adid;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }
}
