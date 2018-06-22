/*
 * Copyright 2014 trinea.cn All right reserved. This software is the confidential and proprietary information of
 * trinea.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with trinea.cn.
 */
package com.yn.reader.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yn.reader.R;
import com.yn.reader.model.home.Banner;
import com.yn.reader.util.GlideUtils;

import java.util.List;


/**
 * ImagePagerAdapter
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-2-23
 */
public class ImagePagerAdapter extends RecyclingPagerAdapter {

    private Context       context;
    private List<Banner> imageIdList;

    private int           size;
    private boolean       isInfiniteLoop;

    public ImagePagerAdapter(Context context, List<Banner> imageIdList) {
        this.context = context;
        this.imageIdList = imageIdList;
        this.size = imageIdList.size();
        isInfiniteLoop = false;
    }

    @Override
    public int getCount() {
        // Infinite loop
        return isInfiniteLoop ? Integer.MAX_VALUE : imageIdList.size();
    }

    /**
     * get really position
     * 
     * @param position
     * @return
     */
    private int getPosition(int position) {
        return isInfiniteLoop ? position % size : position;
    }

    @Override
    public View getView(int position, View view, ViewGroup container) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = holder.imageView = new ImageView(context);
            view.setTag(R.id.tag_holder,holder);
        } else {
            holder = (ViewHolder)view.getTag(R.id.tag_holder);
        }
        Banner banner = getItem(position);
        if(banner!=null&&!TextUtils.isEmpty(banner.getImage())) {
            GlideUtils.load(view.getContext(), banner.getImage(), holder.imageView);
        }
        return view;
    }

    private Banner getItem(int position){
        return imageIdList.get(getPosition(position));
    }

    private static class ViewHolder {

        ImageView imageView;
    }

    /**
     * @return the isInfiniteLoop
     */
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }
}
