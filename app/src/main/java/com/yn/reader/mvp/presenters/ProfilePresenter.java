package com.yn.reader.mvp.presenters;

import com.yn.reader.mvp.views.BaseView;
import com.yn.reader.mvp.views.ProfileView;

/**
 * Created by luhe on 2018/3/29.
 */

public class ProfilePresenter extends BasePresenter{
    private ProfileView mProfileView;
    public ProfilePresenter(ProfileView profileView){
        mProfileView = profileView;
    }
    @Override
    public BaseView getBaseView() {
        return mProfileView;
    }

    @Override
    public void success(Object o) {
//        if (o instanceof UserInitializedInfo){
//            mProfileView.onLoadUserInitializedInfo(((UserInitializedInfo) o).getData());
//        }
    }

    @Override
    public void detachView() {
        unSubscribe();
    }

//    public void reloadUserInfo() {
//        addSubscription(MiniRest.getInstance().getUserInfo());
//    }
}
