package com.yn.reader.view.controller.base;

import android.content.Context;
import android.view.View;

/**
 * Created by luhe on 2018/5/26.
 */

public abstract class BaseController {
    protected Context mContext;
    protected View mContentView;

    public BaseController(View view) {
        mContentView = view;
        mContext = mContentView.getContext();
        initElement();
        initData();
    }

    protected abstract void initElement();

    protected abstract void initData();
}
