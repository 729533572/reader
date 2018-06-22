package com.yn.reader.model.home;

import com.yn.reader.model.buy.BuyAdvertisement;

/**
 * 首页顶部banner
 * Created by sunxy on 2017/12/26.
 */

public class Banner {
    private String image;
    private String link;
    private String link_type;

    public Banner(){}

    public Banner(BuyAdvertisement buyAdvertisement){
        this.image = buyAdvertisement.getImage();
        this.link = buyAdvertisement.getLink_content();
        this.link_type = buyAdvertisement.getLink_type();
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink_type() {
        return link_type;
    }

    public void setLink_type(String link_type) {
        this.link_type = link_type;
    }
}
