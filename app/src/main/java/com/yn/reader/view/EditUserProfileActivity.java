package com.yn.reader.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hysoso.www.utillibrary.StringUtil;
import com.hysoso.www.viewlibrary.MFSRoundImageView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.rey.material.app.BottomSheetDialog;
import com.yn.reader.BuildConfig;
import com.yn.reader.R;
import com.yn.reader.base.BaseMvpActivity;
import com.yn.reader.mvp.presenters.BasePresenter;
import com.yn.reader.mvp.presenters.EditUserProfilePresenter;
import com.yn.reader.mvp.views.EditUserProfileView;
import com.yn.reader.model.LoginResult;
import com.yn.reader.model.dao.UserInfo;
import com.yn.reader.util.AppPreference;
import com.yn.reader.util.ComUtils;
import com.yn.reader.util.Constant;
import com.yn.reader.util.DeviceUtil;
import com.yn.reader.util.GenderType;
import com.yn.reader.util.GlideUtils;
import com.yn.reader.util.UserInfoManager;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created : lts .
 * Date: 2018/1/2
 * Email: lts@aso360.com
 */

public class EditUserProfileActivity extends BaseMvpActivity implements EditUserProfileView, TextWatcher {
    private static final int IMAGE_PICKER = 1;
    @BindView(R.id.iv_icon)
    MFSRoundImageView mIvIcon;
    @BindView(R.id.nickName)
    EditText mNickName;
    @BindView(R.id.gender)
    TextView mGender;
    private EditUserProfilePresenter mPresenter;
    private BottomSheetDialog mSheetDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        ButterKnife.bind(this);

        setToolbarTitle(R.string.toolbar_title_edit_profile);
        mNickName.addTextChangedListener(this);
        mPresenter = new EditUserProfilePresenter(this);
        initData();
    }

    private void initData() {
        if (StringUtil.isEmpty(UserInfoManager.getInstance().getUser().getAvatar()))
            mIvIcon.setImageResource(R.drawable.default_avatar);
        else
            GlideUtils.load(getContext(), UserInfoManager.getInstance().getUser().getAvatar(), mIvIcon);

        if (!TextUtils.isEmpty(UserInfoManager.getInstance().getUser().getNickname())) {
            mNickName.setText(UserInfoManager.getInstance().getUser().getNickname());
            mNickName.setSelection(UserInfoManager.getInstance().getUser().getNickname().length() > 10 ? 10 : UserInfoManager.getInstance().getUser().getNickname().length());//将光标移至文字末尾
        }
        mNickName.setOnEditorActionListener(actionListener);
        mNickName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    mNickName.setSelection(UserInfoManager.getInstance().getUser().getNickname().length() > 10 ? 10 : UserInfoManager.getInstance().getUser().getNickname().length());//将光标移至文字末尾
            }
        });
        mGender.setText(GenderType.getGenderType(UserInfoManager.getInstance().getUser().getSex()));
    }

    private TextView.OnEditorActionListener actionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (event != null && event.getAction() == KeyEvent.ACTION_DOWN) {
                String nickName = mNickName.getText().toString().trim();
                if (!TextUtils.isEmpty(nickName)) {
                    mPresenter.upDateNickName(nickName);
                } else {
                    Toast.makeText(EditUserProfileActivity.this, "昵称不要为空啦", Toast.LENGTH_LONG).show();
                }
            }

            return false;
        }
    };

    @Override
    public void error() {

    }


    @Override
    public Activity getContext() {
        return this;
    }

    @OnClick({R.id.nickName, R.id.gender, R.id.iv_icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.gender:
                showChangeGenderDialog();
                break;
            case R.id.iv_icon:
                selectAlbum();
                break;
        }
    }

    void selectAlbum() {
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, IMAGE_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null && !images.isEmpty()) {
                    File file = new File(images.get(0).path);
                    Glide.with(this).load(file).into(mIvIcon);
                    mPresenter.updateUserInfoHeader(file);
                }
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getToken(String time, AppPreference ap) {
        return ComUtils.getToken(ap.getString(Constant.OPEN_ID), ap.getString(Constant.UNION_ID),
                Constant.PACKAGE_NAME, BuildConfig.VERSION_NAME, DeviceUtil.getIMEI(this), time);
    }

    /**
     * 修改性别
     */
    private void showChangeGenderDialog() {
        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mNickName.getWindowToken(), 0);

        mSheetDialog = new BottomSheetDialog(this);

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_change_gender, null, false);

        final TextView male = view.findViewById(R.id.male);
        final TextView female = view.findViewById(R.id.female);
        TextView cancel = view.findViewById(R.id.cancel);

        male.setSelected(mGender.getText().equals("男"));
        female.setSelected(mGender.getText().equals("女"));

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.male:
                        mSheetDialog.dismiss();
                        String s = mGender.getText().toString().trim();
                        if (s.equals("男")) {
                            return;
                        }
                        mGender.setText("男");
                        v.setSelected(true);
                        female.setSelected(false);
                        mPresenter.upDateGender(String.valueOf(1));
                        break;
                    case R.id.female:
                        mSheetDialog.dismiss();
                        String female = mGender.getText().toString().trim();
                        if (female.equals("女")) {
                            return;
                        }
                        mGender.setText("女");
                        male.setSelected(false);
                        v.setSelected(true);
                        mPresenter.upDateGender(String.valueOf(2));
                        break;
                    case R.id.cancel:
                        mSheetDialog.dismiss();
                        break;
                }
            }
        };
        male.setOnClickListener(onClickListener);
        female.setOnClickListener(onClickListener);
        cancel.setOnClickListener(onClickListener);

        mSheetDialog.setContentView(view);
        mSheetDialog.heightParam(ViewGroup.LayoutParams.WRAP_CONTENT);
        mSheetDialog.show();
    }


    @Override
    public void upDateUserProfile(LoginResult loginResult) {
        if (loginResult.getStatus() == 1) {
            UserInfo data = loginResult.getData();
            UserInfoManager.getInstance().save(data);
        } else {
            Toast.makeText(this, getResources().getString(R.string.update_userprofile_error), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.toString().length() > 10) {
            mNickName.removeTextChangedListener(this);
            mNickName.setText(editable.toString().substring(0, 10));
            mNickName.setSelection(10);
            mNickName.addTextChangedListener(this);
            Toast.makeText(this, getResources().getString(R.string.mutil_message), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public BasePresenter getPresenter() {
        return mPresenter;
    }
}
