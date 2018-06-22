//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.yn.reader.widget.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yn.reader.R;
import com.yn.reader.model.chapter.BookContentGroup;
import com.yn.reader.util.AppPreference;
import com.yn.reader.util.IntentUtils;
import com.yn.reader.view.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BuyPopOutLand extends PopupWindow {
    private Activity mContext;
    private View view;
    private BookContentGroup mBookContentGroup;

    @BindView(R.id.tv_price)
    TextView tv_price;

    public Context getContext() {
        return mContext;
    }

    public BuyPopOutLand(Activity context) {
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mContext = context;

        view = LayoutInflater.from(mContext).inflate(AppPreference.getInstance().isNightModel() ? R.layout.view_pop_buy_out_land_night : R.layout.view_pop_buy_out_land, null);
        this.setContentView(view);
        ButterKnife.bind(this, getContentView());

        setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_pop_checkaddshelf_bg));
        setFocusable(true);
        setTouchable(true);
        setAnimationStyle(R.style.anim_pop_windowlight);

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                tv_price.setText("");
            }
        });
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    public void setData(BookContentGroup bookContentGroup) {
        mBookContentGroup = bookContentGroup;
        tv_price.setText(bookContentGroup.getPrice() + "点");
    }

    @OnClick(R.id.btn_land_at_once)
    public void landAtOnce() {
        dismiss();
        IntentUtils.startActivity(getContext(), LoginActivity.class);
    }
}
