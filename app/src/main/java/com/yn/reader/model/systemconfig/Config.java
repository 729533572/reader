package com.yn.reader.model.systemconfig;

import android.text.TextUtils;

import com.yn.reader.util.AppPreference;

/**
 * Created : lts .
 * Date: 2018/1/9
 * Email: lts@aso360.com
 */

public class Config {

    /**
     * code : 1000
     * msg : 验证成功
     * isApprove : true
     * isWxAvailable : true
     * isAliAvailable : true
     * appurl : https://itunes.apple.com/cn/app/id1277561160?mt=8
     * maxShareTimes : 3
     * shareMoney : 20
     * maxVideoAdTimes : 3
     * videoAdAward : 20
     * adVideo : google
     * adImage : google
     * adVideoAppId : ca-app-pub-6565187112513035~6669895261
     * adImageAppId : ca-app-pub-6565187112513035~6669895261
     * adVideoAdId : ca-app-pub-6565187112513035/6946528174
     * adImageAdId : ca-app-pub-6565187112513035/8642753220
     * shareTitle : 我给你分享一个好玩的App~
     * shareUrl : https://itunes.apple.com/cn/app/id1277561160?mt=8
     * jpushkey : 4ea38acedcdc1e22c45b2fc9
     * gameover_video : 0
     * bUpdate : false
     * updateDes :
     * updateUrl :
     */

    private int code;
    private String msg;
    private boolean isApprove;
    private boolean isWxAvailable;
    private boolean isAliAvailable;
    private String appurl;
    private int maxShareTimes;
    private int shareMoney;
    private int maxVideoAdTimes;
    private int videoAdAward;
    private String adVideo;
    private String adImage;
    private String adVideoAppId;
    private String adImageAppId;
    private String adVideoAdId;
    private String adImageAdId;
    private String shareTitle;
    private String shareUrl;
    private String jpushkey;
    private int gameover_video;
    private boolean bUpdate;
    private String updateDes;
    private String updateUrl;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isIsApprove() {
        return isApprove;
    }

    public void setIsApprove(boolean isApprove) {
        this.isApprove = isApprove;
    }

    public boolean isIsWxAvailable() {
        return isWxAvailable;
    }

    public void setIsWxAvailable(boolean isWxAvailable) {
        this.isWxAvailable = isWxAvailable;
    }

    public boolean isIsAliAvailable() {
        return isAliAvailable;
    }

    public void setIsAliAvailable(boolean isAliAvailable) {
        this.isAliAvailable = isAliAvailable;
    }

    public String getAppurl() {
        return appurl;
    }

    public void setAppurl(String appurl) {
        this.appurl = appurl;
    }

    public int getMaxShareTimes() {
        return maxShareTimes;
    }

    public void setMaxShareTimes(int maxShareTimes) {
        this.maxShareTimes = maxShareTimes;
    }

    public int getShareMoney() {
        return shareMoney;
    }

    public void setShareMoney(int shareMoney) {
        this.shareMoney = shareMoney;
    }

    public int getMaxVideoAdTimes() {
        return maxVideoAdTimes;
    }

    public void setMaxVideoAdTimes(int maxVideoAdTimes) {
        this.maxVideoAdTimes = maxVideoAdTimes;
    }

    public int getVideoAdAward() {
        return videoAdAward;
    }

    public void setVideoAdAward(int videoAdAward) {
        this.videoAdAward = videoAdAward;
    }

    public String getAdVideo() {
        return adVideo;
    }

    public void setAdVideo(String adVideo) {
        this.adVideo = adVideo;
    }

    public String getAdImage() {
        return adImage;
    }

    public void setAdImage(String adImage) {
        this.adImage = adImage;
    }

    public String getAdVideoAppId() {
        return adVideoAppId;
    }

    public void setAdVideoAppId(String adVideoAppId) {
        this.adVideoAppId = adVideoAppId;
    }

    public String getAdImageAppId() {
        return adImageAppId;
    }

    public void setAdImageAppId(String adImageAppId) {
        this.adImageAppId = adImageAppId;
    }

    public String getAdVideoAdId() {
        return adVideoAdId;
    }

    public void setAdVideoAdId(String adVideoAdId) {
        this.adVideoAdId = adVideoAdId;
    }

    public String getAdImageAdId() {
        return adImageAdId;
    }

    public void setAdImageAdId(String adImageAdId) {
        this.adImageAdId = adImageAdId;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
        if(!TextUtils.isEmpty(shareTitle)) {
            AppPreference.getInstance().setShareTitle(shareTitle);
        }
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
        if(!TextUtils.isEmpty(shareUrl)) {
            AppPreference.getInstance().setShareUrl(shareUrl);
        }
    }

    public String getJpushkey() {
        return jpushkey;
    }

    public void setJpushkey(String jpushkey) {
        this.jpushkey = jpushkey;
    }

    public int getGameover_video() {
        return gameover_video;
    }

    public void setGameover_video(int gameover_video) {
        this.gameover_video = gameover_video;
    }

    public boolean isBUpdate() {
        return bUpdate;
    }

    public void setBUpdate(boolean bUpdate) {
        this.bUpdate = bUpdate;
    }

    public String getUpdateDes() {
        return updateDes;
    }

    public void setUpdateDes(String updateDes) {
        this.updateDes = updateDes;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }
}
