package com.yn.reader.model.aso;

/**
 * 自主开屏广告
 * Created by sunxy on 2018/1/13.
 */

public class AsoAdResponse  {


    /**
     * status : 1
     * data : {"adid":"38","applogo":"http://59.110.26.82/upload/ads/logo/5/1/af9a5ff1d856a8803ddc42d3f27132e2.gif","key":"sdfgdfgdfg","adname":"姜东旭开屏广告","adcontent":"姜东旭开屏广告","adtype":"1003","img_horizontal":"http://59.110.26.82/upload/ads/1003/5/38/dc99e7c1891c5c3d3d112c1a34b9e067.png","img_vertical":"http://59.110.26.82/upload/ads/1003/5/38/5fb9461a2e28965767a0b67db6b02a48.png","linktype":"1","link":"http://baidu.com","duration":"10"}
     */

    private int status;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * adid : 38
         * applogo : http://59.110.26.82/upload/ads/logo/5/1/af9a5ff1d856a8803ddc42d3f27132e2.gif
         * key : sdfgdfgdfg
         * adname : 姜东旭开屏广告
         * adcontent : 姜东旭开屏广告
         * adtype : 1003
         * img_horizontal : http://59.110.26.82/upload/ads/1003/5/38/dc99e7c1891c5c3d3d112c1a34b9e067.png
         * img_vertical : http://59.110.26.82/upload/ads/1003/5/38/5fb9461a2e28965767a0b67db6b02a48.png
         * linktype : 1
         * link : http://baidu.com
         * duration : 10
         */

        private String adid;
        private String applogo;
        private String key;
        private String adname;
        private String adcontent;
        private String adtype;
        private String img_horizontal;
        private String img_vertical;
        private String linktype;
        private String link;
        private int duration;

        public String getAdid() {
            return adid;
        }

        public void setAdid(String adid) {
            this.adid = adid;
        }

        public String getApplogo() {
            return applogo;
        }

        public void setApplogo(String applogo) {
            this.applogo = applogo;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getAdname() {
            return adname;
        }

        public void setAdname(String adname) {
            this.adname = adname;
        }

        public String getAdcontent() {
            return adcontent;
        }

        public void setAdcontent(String adcontent) {
            this.adcontent = adcontent;
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

        public String getLinktype() {
            return linktype;
        }

        public void setLinktype(String linktype) {
            this.linktype = linktype;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }
    }
}
