package com.yn.reader.view;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.hysoso.www.utillibrary.StringUtil;
import com.hysoso.www.utillibrary.ToastUtil;
import com.yn.reader.R;
import com.yn.reader.base.BaseActivity;
import com.yn.reader.login.utils.RegexUtils;
import com.yn.reader.login.utils.ToastUtils;
import com.yn.reader.login.utils.VerifyCodeManager;
import com.yn.reader.login.views.CleanEditText;
import com.yn.reader.mvp.presenters.RegisterPresenter;
import com.yn.reader.mvp.views.RegisterView;
import com.yn.reader.model.BaseData;
import com.yn.reader.util.AppPreference;
import com.yn.reader.util.Constant;
import com.yn.reader.util.IntentUtils;

import butterknife.ButterKnife;


/**
 * @desc 注册界面以及忘记密码界面
 * 功能描述：一般会使用手机登录，通过获取手机验证码，跟服务器交互完成注册
 * Created by devilwwj on 16/1/24.
 */
public class SignUpActivity extends BaseActivity implements OnClickListener, RegisterView {
    private static final String TAG = "SignupActivity";
    // 界面控件
    private CleanEditText phoneEdit;
    private CleanEditText passwordEdit;
    private CleanEditText verifyCodeEdit;
    private Button getVerifiCodeButton;

    private VerifyCodeManager codeManager;
    RegisterPresenter registerPresenter;
    private int registerOrForgetPassword = Constant.TYPE_REGISTER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        handleIntent();
        if (registerOrForgetPassword==Constant.TYPE_REGISTER)
            setToolbarTitle(R.string.create_account);
        else if (registerOrForgetPassword==Constant.TYPE_FORGET_PASSWORD)
            setToolbarTitle(R.string.find_password);
        initViews();
        registerPresenter = new RegisterPresenter(this);
        codeManager = new VerifyCodeManager(this, phoneEdit, getVerifiCodeButton, registerPresenter, registerOrForgetPassword);

    }

    private void handleIntent() {
        registerOrForgetPassword = getIntent().getIntExtra(Constant.REGISTER_OR_FORGET_PASSWORD, Constant.TYPE_REGISTER);
    }

    /**
     * 通用findViewById,减少重复的类型转换
     *
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public final <E extends View> E getView(int id) {
        try {
            return (E) findViewById(id);
        } catch (ClassCastException ex) {
            Log.e(TAG, "Could not cast View to concrete class.", ex);
            throw ex;
        }
    }


    private void initViews() {

        getVerifiCodeButton = getView(R.id.btn_send_verifi_code);
        getVerifiCodeButton.setOnClickListener(this);
        phoneEdit = getView(R.id.et_phone);
        phoneEdit.setImeOptions(EditorInfo.IME_ACTION_NEXT);// 下一步
        verifyCodeEdit = getView(R.id.et_verifiCode);
        verifyCodeEdit.setImeOptions(EditorInfo.IME_ACTION_NEXT);// 下一步
        passwordEdit = getView(R.id.et_password);
        passwordEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        passwordEdit.setImeOptions(EditorInfo.IME_ACTION_GO);
        passwordEdit.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                // 点击虚拟键盘的done
                if (actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_GO) {
                    commit();
                }
                return false;
            }
        });
        Button register = getView(R.id.btn_create_account);
        if(registerOrForgetPassword==Constant.TYPE_FORGET_PASSWORD){
            register.setText(R.string.reset_pwd);
        }
        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                commit();
            }
        });
        String accountPassed = getIntent().getStringExtra(Constant.KEY_WORD);
        phoneEdit.setText(StringUtil.isEmpty(accountPassed)?AppPreference.getInstance().getAccount():accountPassed);
    }

    private void commit() {
        String phone = phoneEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();
        String code = verifyCodeEdit.getText().toString().trim();

        if (checkInput(phone, password, code)) {
            if (registerOrForgetPassword == Constant.TYPE_REGISTER) {
                registerPresenter.register(this, phone, code, password, password);
            } else {
                registerPresenter.forgetPassword(this, phone, code, password, password);
            }
        }
    }

    private boolean checkInput(String phone, String password, String code) {
        if (TextUtils.isEmpty(phone)) { // 电话号码为空
            ToastUtils.showShort(this, R.string.tip_phone_can_not_be_empty);
        } else {
            if (!RegexUtils.checkMobile(phone)) { // 电话号码格式有误
                ToastUtils.showShort(this, R.string.tip_phone_regex_not_right);
            } else if (TextUtils.isEmpty(code)) { // 验证码不正确
                ToastUtils.showShort(this, R.string.tip_please_input_code);
            } else if (password.length() < 6 || password.length() > 32
                    || TextUtils.isEmpty(password)) { // 密码格式
                ToastUtils.showShort(this,
                        R.string.tip_please_input_6_32_password);
            } else {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_verifi_code:
                codeManager.getVerifyCode(VerifyCodeManager.REGISTER);
                break;

            default:
                break;
        }
    }

    @Override
    public void onSmsCodeSuccess(BaseData baseData) {
        if (baseData.getStatus() != 1) {
            if (!TextUtils.isEmpty(baseData.getErr_msg())) {
                ToastUtils.showLong(this, baseData.getErr_msg());
            }
        } else {
            ToastUtils.showLong(this, R.string.send_sms_success);
        }
    }

    @Override
    public void findBackPasswordSuccess() {
        ToastUtil.showShort(this,"密码已找回");
        IntentUtils.popPreviousActivity(this);
    }

    @Override
    public void registerSuccess() {
        ToastUtil.showShort(this,"注册成功");
        IntentUtils.popPreviousActivity(this);
    }

    @Override
    public Activity getContext() {
        return this;
    }
}
