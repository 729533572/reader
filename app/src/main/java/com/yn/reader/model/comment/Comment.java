package com.yn.reader.model.comment;

import com.yn.reader.network.api.MiniRest;

/**
 * 评论
 * Created by sunxy on 2018/3/16.
 */

public class Comment {
    private int commentid;
    private String commentcontent;
    private String commentdate;
    private String userid;
    private String nickname;
    private int num;//赞总数
    private int hasfavorite;//当前用户是否赞过
    private int ismine;
    private String avatar;

    public int getCommentid() {
        return commentid;
    }

    public void setCommentid(int commentid) {
        this.commentid = commentid;
    }

    public String getCommentcontent() {
        return commentcontent;
    }

    public void setCommentcontent(String commentcontent) {
        this.commentcontent = commentcontent;
    }

    public String getCommentdate() {
        return commentdate;
    }

    public void setCommentdate(String commentdate) {
        this.commentdate = commentdate;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getHasfavorite() {
        return hasfavorite;
    }

    public void setHasfavorite(int hasfavorite) {
        this.hasfavorite = hasfavorite;
    }

    public int getIsmine() {
        return ismine;
    }

    public void setIsmine(int ismine) {
        this.ismine = ismine;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = MiniRest.MINI_HOST_END_POINT+avatar;
    }
}
