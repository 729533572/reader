//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.yn.reader.widget.popupwindow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yn.reader.R;
import com.yn.reader.util.ReadBookControl;

public class FontPop extends PopupWindow {
    private Context mContext;
    private View view;

    private ReadBookControl readBookControl;

    public interface OnChangeProListener {
        void textChange(int index);
    }

    private OnChangeProListener changeProListener;

    public FontPop(Context context, @NonNull OnChangeProListener changeProListener) {
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.mContext = context;
        this.changeProListener = changeProListener;

        view = LayoutInflater.from(mContext).inflate(R.layout.view_pop_font, null);
        this.setContentView(view);
        initData();
        bindView();

        setFocusable(true);
        setTouchable(true);
        setAnimationStyle(R.style.anim_pop_windowlight);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }


    private void bindView() {
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        LinearLayout fontSizeLayout = view.findViewById(R.id.font_size_layout);
        int childCount = fontSizeLayout.getChildCount();
        int selectIndex = readBookControl.getTextKindIndex();
        for (int i = 0; i < childCount; i++) {
            final int position = i;
            FrameLayout innerLayout = (FrameLayout) fontSizeLayout.getChildAt(i);
            final ImageView fontImage = (ImageView) innerLayout.getChildAt(0);
            final TextView fontSize = (TextView) innerLayout.getChildAt(1);
            if (selectIndex == i) {
                fontImage.setSelected(true);
                fontSize.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            } else {
                fontImage.setSelected(false);
                fontSize.setTextColor(mContext.getResources().getColor(R.color.font_size_color));
            }
            innerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    readBookControl.setTextKindIndex(position);
                    changeProListener.textChange(readBookControl.getTextKindIndex());
                    bindView();
                }
            });
        }

    }

    private void initData() {
        readBookControl = ReadBookControl.getInstance();
    }
}