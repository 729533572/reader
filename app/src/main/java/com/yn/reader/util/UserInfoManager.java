package com.yn.reader.util;

import com.yn.reader.db.UserInfoDao;
import com.yn.reader.model.dao.DbHelper;
import com.yn.reader.model.dao.UserInfo;

/**
 * Created by luhe on 2018/4/12.
 */

public class UserInfoManager {
    private static UserInfoManager mInstance = null;
    private UserInfoDao mUserInfoDao;

    public static UserInfoManager getInstance() {
        if (mInstance == null) {
            synchronized (UserInfoManager.class) {
                if (mInstance == null) mInstance = new UserInfoManager();
            }
        }
        return mInstance;
    }

    private UserInfoManager() {
        mUserInfoDao = DbHelper.getInstance().getDaoSession().getUserInfoDao();
    }

    public boolean isLanded() {
        return mUserInfoDao.loadAll().size() == 1;
    }

    public UserInfo getUser() {
        return isLanded() ? mUserInfoDao.loadAll().get(0) : null;
    }

    public long getUid() {
        return isLanded() ? getUser().getUserid() : -1;
    }

    public String getKey() {
        return isLanded() ? getUser().getKey() : "";
    }

    public void save(UserInfo userInfo) {
        synchronized (this) {
            mUserInfoDao.deleteAll();
            mUserInfoDao.insert(userInfo);
        }
    }
}
