package com.yn.reader.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.hysoso.www.utillibrary.StringUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yn.reader.R;
import com.yn.reader.base.BaseActivity;
import com.yn.reader.db.UserInfoDao;
import com.yn.reader.event.LoginSuccessEvent;
import com.yn.reader.login.utils.RegexUtils;
import com.yn.reader.login.utils.ToastUtils;
import com.yn.reader.login.utils.Utils;
import com.yn.reader.login.views.CleanEditText;
import com.yn.reader.mvp.presenters.LoginPresenter;
import com.yn.reader.mvp.views.LoginView;
import com.yn.reader.model.LoginResult;
import com.yn.reader.model.WeChatInfo;
import com.yn.reader.model.WeChatToken;
import com.yn.reader.model.dao.DbHelper;
import com.yn.reader.model.dao.UserInfo;
import com.yn.reader.service.SynchronizeCollectionDataService;
import com.yn.reader.util.AppPreference;
import com.yn.reader.util.BusProvider;
import com.yn.reader.util.ComUtils;
import com.yn.reader.util.Constant;

import butterknife.ButterKnife;

import static android.view.View.OnClickListener;

/**
 * @desc 登录界面
 * Created by devilwwj on 16/1/24.
 */
public class LoginActivity extends BaseActivity implements OnClickListener, LoginView {
    private static final int REQUEST_CODE_TO_REGISTER = 0x001;

    // 界面控件
    private CleanEditText accountEdit;
    private CleanEditText passwordEdit;


    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loginPresenter = new LoginPresenter(this);
        setToolbarTitle(R.string.login);
        initViews();
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        accountEdit = this.findViewById(R.id.et_email_phone);
        accountEdit.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        accountEdit.setTransformationMethod(HideReturnsTransformationMethod
                .getInstance());
        passwordEdit = this.findViewById(R.id.et_password);
        passwordEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        passwordEdit.setImeOptions(EditorInfo.IME_ACTION_GO);
        passwordEdit.setTransformationMethod(PasswordTransformationMethod
                .getInstance());
        passwordEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_GO) {
                    clickLogin();
                }
                return false;
            }
        });
        accountEdit.setText(AppPreference.getInstance().getAccount());
        if (accountEdit.getText().length() >= 11) passwordEdit.requestFocus();
    }

    private void clickLogin() {
        String account = accountEdit.getText().toString();
        if (!StringUtil.isEmpty(account)) AppPreference.getInstance().setAccount(account);
        String password = passwordEdit.getText().toString();
        if (checkInput(account, password)) {
            loginPresenter.phoneLogin(account, password);
        }
    }

    /**
     * 检查输入
     *
     * @param account
     * @param password
     * @return
     */
    public boolean checkInput(String account, String password) {
        // 账号为空时提示
        if (account == null || account.trim().equals("")) {
            Toast.makeText(this, R.string.tip_account_empty, Toast.LENGTH_LONG)
                    .show();
        } else {
            // 账号不匹配手机号格式（11位数字且以1开头）
            if (!RegexUtils.checkMobile(account)) {
                Toast.makeText(this, R.string.tip_account_regex_not_right,
                        Toast.LENGTH_LONG).show();
            } else if (password == null || password.trim().equals("")) {
                Toast.makeText(this, R.string.tip_password_can_not_be_empty,
                        Toast.LENGTH_LONG).show();
            } else {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_login:
                clickLogin();
                break;
            case R.id.tv_wechat:
                clickLoginWexin();
                break;
            case R.id.iv_qq:
                clickLoginQQ();
                break;
            case R.id.iv_sina:
                loginThirdPlatform(SHARE_MEDIA.SINA);
                break;
            case R.id.tv_create_account:
                enterRegister();
                break;
            case R.id.tv_forget_password:
                enterForgetPwd();
                break;
            default:
                break;
        }
    }

    /**
     * 点击使用QQ快速登录
     */
    private void clickLoginQQ() {
        if (!Utils.isQQClientAvailable(this)) {
            ToastUtils.showShort(LoginActivity.this,
                    getString(R.string.no_install_qq));
        } else {
            loginThirdPlatform(SHARE_MEDIA.QZONE);
        }
    }

    /**
     * 点击使用微信登录
     */
    private void clickLoginWexin() {
        if (!Utils.isWeixinAvilible(this)) {
            ToastUtils.showShort(LoginActivity.this,
                    getString(R.string.no_install_wechat));
        } else {
            loginThirdPlatform(SHARE_MEDIA.WEIXIN);
            ComUtils.login();
            finish();
        }
    }

    /**
     * 跳转到忘记密码
     */
    private void enterForgetPwd() {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra(Constant.REGISTER_OR_FORGET_PASSWORD, Constant.TYPE_FORGET_PASSWORD);
        intent.putExtra(Constant.KEY_WORD, accountEdit.getText().toString());
        startActivity(intent);
    }

    /**
     * 跳转到注册页面
     */
    private void enterRegister() {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra(Constant.REGISTER_OR_FORGET_PASSWORD, Constant.TYPE_REGISTER);
        intent.putExtra(Constant.KEY_WORD, accountEdit.getText().toString());
        startActivityForResult(intent, REQUEST_CODE_TO_REGISTER);
    }


    /**
     * 授权。如果授权成功，则获取用户信息
     *
     * @param platform
     */
    private void loginThirdPlatform(final SHARE_MEDIA platform) {

    }


    @Override
    public Activity getContext() {
        return this;
    }

    @Override
    public void getWeChatSuccess(WeChatToken weChatToken) {

    }

    @Override
    public void loginSuccess(LoginResult loginResult) {
        if (loginResult.getStatus() == 1) {
            UserInfo userInfo = loginResult.getData();
            UserInfoDao userInfoDao = DbHelper.getInstance().getDaoSession().getUserInfoDao();
            userInfoDao.deleteAll();
            userInfoDao.insert(userInfo);

            Intent intent = new Intent(this, SynchronizeCollectionDataService.class);
            startService(intent);

            BusProvider.getInstance().post(new LoginSuccessEvent(userInfo));
            finish();
        } else {
            Toast.makeText(this, getResources().getString(R.string.login_service_error), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void getWeChatInfoSuccess(WeChatInfo weChatInfo) {

    }

    @Override
    public void phoneLoginSuccess(UserInfo userInfo) {

    }
}
