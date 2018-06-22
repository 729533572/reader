//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.yn.reader.widget.popupwindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.yn.reader.R;


public class ReadBookMenuMorePop extends PopupWindow {
    private Context mContext;
    private View view;

    private LinearLayout llDownload;

    public ReadBookMenuMorePop(Context context){
        super(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.mContext = context;
        view = LayoutInflater.from(mContext).inflate(R.layout.view_pop_menumore,null);
        this.setContentView(view);

        initView();

        setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_pop_checkaddshelf_bg));
        setFocusable(true);
        setTouchable(true);
        setAnimationStyle(R.style.anim_pop_windowmenumore);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private void initView() {
        llDownload = (LinearLayout) view.findViewById(R.id.ll_download);
    }

    public void setOnClickDownload(View.OnClickListener clickDownload){
        llDownload.setOnClickListener(clickDownload);
    }
}
