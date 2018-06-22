package com.yn.reader.model.home;

import java.util.List;

/**
 * 导航列表
 * Created by sunxy on 2017/12/26.
 */

public class HomeList {
    private List<Banner> banners;
    private List<Channel> channels;
    private List<HotCategory> hotchannels;

    public List<Banner> getBanners() {
        return banners;
    }

    public void setBanners(List<Banner> banners) {
        this.banners = banners;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public void setHotchannels(List<HotCategory> hotchannels) {
        this.hotchannels = hotchannels;
    }

    public List<HotCategory> getHotchannels() {
        return hotchannels;
    }
}
