package com.yn.reader.model;


import com.yn.reader.model.dao.UserInfo;

/**
 * Created : lts .
 * Date: 2017/12/28
 * Email: lts@aso360.com
 */

public class LoginResult extends BaseData{

    /**
     * status : 1
     * data : {"userid":2,"username":"wx_1513564801","openid":"abcdefghijklmnopqrst","nickname":"耀用心","sex":1,"avatar":"/image/weixinavatar/2017-12-18/7563a3796d76a4ce942690e890db83fe.jpg","province":"江苏省","city":"南京市","country":"中国"}
     */
    private UserInfo data;

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }


}
