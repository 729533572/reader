package com.hysoso.www.viewlibrary;

import android.content.Context;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.AttributeSet;

/**
 * Created by luhe on 17/3/13.
 */

public class MFSContentLoadingProgressBar extends ContentLoadingProgressBar {
    private String mFailureTag;
    private String mSuccessTag;
    public MFSContentLoadingProgressBar(Context context) {
        super(context);
    }

    public MFSContentLoadingProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTag(String failure,String success) {
        mFailureTag = failure;
        mSuccessTag = success;
    }

    public String getFailureTag() {
        return mFailureTag;
    }

    public String getSuccessTag() {
        return mSuccessTag;
    }
    public void clearTags(){
        mFailureTag = "";
        mSuccessTag = "";
    }
    public void showProgress() {
        setVisibility(VISIBLE);
    }
    public void hideProgress() {
        setVisibility(GONE);
    }
    public Boolean isShowing(){
        if (getVisibility()==VISIBLE)return true;
        else return false;
    }
}
