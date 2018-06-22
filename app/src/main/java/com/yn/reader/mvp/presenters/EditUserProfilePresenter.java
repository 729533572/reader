package com.yn.reader.mvp.presenters;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.hysoso.www.utillibrary.FileUtil;
import com.hysoso.www.utillibrary.ImageUtil;
import com.hysoso.www.utillibrary.LogUtil;
import com.yn.reader.mvp.views.BaseView;
import com.yn.reader.mvp.views.EditUserProfileView;
import com.yn.reader.network.api.MiniRest;
import com.yn.reader.model.LogOutResult;
import com.yn.reader.model.LoginResult;

import java.io.File;

/**
 * Created : lts .
 * Date: 2018/1/3
 * Email: lts@aso360.com
 */

public class EditUserProfilePresenter extends BasePresenter {

    private EditUserProfileView mProfileView;
    private File upload_header_file_end;

    public EditUserProfilePresenter(EditUserProfileView view) {
        this.mProfileView = view;
    }

    /**
     * 修改昵称
     *
     * @param nickName 修改后的昵称
     */
    public void upDateNickName(String nickName) {
        updateUserInfo("nickname", nickName);
    }

    /**
     * 修改性别
     *
     * @param sex
     */
    public void upDateGender(String sex) {
        updateUserInfo("sex", sex);
    }

    public void updateUserInfo(String key, String value) {
        addSubscription(MiniRest.getInstance().upDataUserInfo(key, value));
    }

    public void updateUserInfoHeader(File file) {
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
        final Bitmap header_temp_bitmap = ImageUtil.imageZoom(bitmap, 100);
        LogUtil.e(getClass().getSimpleName(), ImageUtil.calculateImgSize(bitmap) + "/" + ImageUtil.calculateImgSize(header_temp_bitmap));

        String tempFilePath = Environment.getExternalStorageDirectory() + "/headerImg.jpg";
        ImageUtil.compressBmpToFile(header_temp_bitmap, new File(tempFilePath));

        upload_header_file_end = new File(tempFilePath);
        addSubscription(MiniRest.getInstance().upDataUserInfoFile("avatar", upload_header_file_end));
    }

    @Override
    public BaseView getBaseView() {
        return mProfileView;
    }

    @Override
    public void success(Object o) {
        if (o instanceof LogOutResult) {

        } else if (o instanceof LoginResult) {
            LoginResult loginResult = (LoginResult) o;
            mProfileView.upDateUserProfile(loginResult);

            if (upload_header_file_end != null) FileUtil.deleteFile(upload_header_file_end);
        }
    }

    @Override
    public void onFailure(int code, String msg) {
        super.onFailure(code, msg);
        mProfileView.error();
    }

    @Override
    public void detachView() {
        unSubscribe();
    }
}
