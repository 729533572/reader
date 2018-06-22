package com.yn.reader.model.dao;

import com.yn.reader.network.api.MiniRest;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 个人信息
 * Date: 2018/1/2
 * Email: lts@aso360.com
 */
@Entity
public class UserInfo {
    @Id
    private long userid;

    private String nickname;

    private String phone;

    private String email;

    private String avatar;

    private int sex;

    private String desc;

    private String key;

    private int coin;

    private String readvipday;

    private int chapterautobuy;

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        if (avatar.startsWith("/")) avatar = avatar.substring(1, avatar.length());
        this.avatar = MiniRest.MINI_HOST_END_POINT + avatar;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public String getReadvipday() {
        return readvipday;
    }

    public void setReadvipday(String readvipday) {
        this.readvipday = readvipday;
    }

    public int getChapterautobuy() {
        return chapterautobuy;
    }

    public void setChapterautobuy(int chapterautobuy) {
        this.chapterautobuy = chapterautobuy;
    }

    public UserInfo() {
    }

    @Generated(hash = 105933716)
    public UserInfo(long userid, String nickname, String phone, String email,
                    String avatar, int sex, String desc, String key, int coin,
                    String readvipday, int chapterautobuy) {
        this.userid = userid;
        this.nickname = nickname;
        this.phone = phone;
        this.email = email;
        this.avatar = avatar;
        this.sex = sex;
        this.desc = desc;
        this.key = key;
        this.coin = coin;
        this.readvipday = readvipday;
        this.chapterautobuy = chapterautobuy;
    }


}
