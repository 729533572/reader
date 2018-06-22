package com.yn.reader.model.buy;

import com.yn.reader.model.BaseData;

import java.util.List;

/**
 * Created by luhe on 2018/3/22.
 */

public class BuyGroup extends BaseData {

    private List<BuyAdvertisement> ad_top;
    private BuyAdvertisement ad_bottom;
    private List<BuyChoice> vip_list;

    public void setAd_top(List<BuyAdvertisement> ad_top) {
        this.ad_top = ad_top;
    }

    public List<BuyAdvertisement> getAd_top() {
        return ad_top;
    }

    public void setAd_bottom(BuyAdvertisement ad_bottom) {
        this.ad_bottom = ad_bottom;
    }

    public BuyAdvertisement getAd_bottom() {
        return ad_bottom;
    }

    public void setVip_list(List<BuyChoice> vip_list) {
        this.vip_list = vip_list;
    }

    public List<BuyChoice> getVip_list() {
        return vip_list;
    }
}
