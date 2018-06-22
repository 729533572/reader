package com.yn.reader.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yn.reader.R;
import com.yn.reader.base.BaseActivity;
import com.yn.reader.util.IntentUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginTipActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_tip);
        ButterKnife.bind(this);
        setStatusBarColor2White();
    }

    @OnClick(R.id.back_layout)
    public void back() {
        IntentUtils.popPreviousActivity(this);
    }

    @OnClick(R.id.btn_goto_login)
    public void gotoLogin() {
        IntentUtils.startActivityAndFinish(this, LoginActivity.class);
    }
}
