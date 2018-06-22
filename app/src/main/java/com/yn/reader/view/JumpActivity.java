package com.yn.reader.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.yn.reader.R;
import com.yn.reader.base.BaseActivity;
import com.yn.reader.util.ComUtils;
import com.yn.reader.util.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录或者看书跳转
 */
public class JumpActivity extends BaseActivity {

    @BindView(R.id.image_top)
    ImageView mImageTop;
    @BindView(R.id.image_bottom)
    ImageView mImageBottom;
    @BindView(R.id.click_to_continue)
    TextView mClickToContinue;
    int jump_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump);
        ButterKnife.bind(this);
        handleIntent();
        setToolbarTitle("");
    }

    private void handleIntent() {
        Intent intent = getIntent();
        jump_type = intent.getIntExtra(Constant.KEY_JUMP_TYPE, 0);
        switch (jump_type) {
            case Constant.JUMP_TYPE_LOGIN:
                break;
            case Constant.JUMP_TYPE_READ:
                break;
        }
    }

    @OnClick(R.id.click_to_continue)
    public void clickToContinue() {
        switch (jump_type) {
            case Constant.JUMP_TYPE_LOGIN:

                ComUtils.login();
                break;
            case Constant.JUMP_TYPE_READ:

                break;
        }
    }
}
