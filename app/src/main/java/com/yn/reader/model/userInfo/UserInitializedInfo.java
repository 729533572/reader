package com.yn.reader.model.userInfo;

import com.yn.reader.model.BaseData;
import com.yn.reader.model.dao.UserInfo;

/**
 * Created by luhe on 2018/3/29.
 */

public class UserInitializedInfo extends BaseData{
    private UserInfo data;

    public void setData(UserInfo data) {
        this.data = data;
    }

    public UserInfo getData() {
        return data;
    }
}
