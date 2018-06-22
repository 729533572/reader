package com.yn.reader.view;

import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.hysoso.www.utillibrary.StringUtil;
import com.yn.reader.R;
import com.yn.reader.base.BaseActivity;
import com.yn.reader.util.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SharedWebActivity extends BaseActivity {
    private String url;
    private String mTitle;

    @BindView(R.id.activity_shared_web)
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_web);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString(Constant.KEY_WORD);
            mTitle = bundle.getString(Constant.KEY_WORD_ANTHER);
        }

        setToolbarTitle(StringUtil.isEmpty(mTitle) ? "网页" : mTitle);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl(url);
            }
        }, 200);
        if (!mWebView.isFocused()) mWebView.requestFocus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewGroup viewGroup = (ViewGroup) mWebView.getParent();
        if (viewGroup != null) viewGroup.removeView(mWebView);
        mWebView.destroy();
    }
}
