/*
 * Copyright 2016 drakeet. https://github.com/drakeet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yn.reader.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yn.reader.R;
import com.yn.reader.model.home.HotCategory;
import com.yn.reader.util.ComUtils;
import com.yn.reader.util.Constant;
import com.yn.reader.util.GlideUtils;
import com.yn.reader.util.IntentUtils;

import java.util.List;


/**
 * 分类
 */
public class HomeHotCategoryAdapter extends RecyclerView.Adapter<HomeHotCategoryAdapter.ViewHolder> {
    private Context mContext;
    private List<HotCategory> posts;
    private int post_width;

    public HomeHotCategoryAdapter(Context context, List<HotCategory> posts) {
        this.posts = posts;
        mContext = context;
        post_width = (context.getResources().getDisplayMetrics().widthPixels - ComUtils.dip2px(116)) / 3;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hot_category, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final HotCategory post = getItem(position);
        if (post != null) {
            holder.setData(post);
        }

    }

    private HotCategory getItem(int position) {
        if (posts != null && position < posts.size()) {
            return posts.get(position);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        if (posts == null) return 0;
        return posts.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private @NonNull
        ImageView cover;
        private @NonNull
        TextView title;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.hot_category_icon);
            title = itemView.findViewById(R.id.hot_category_title);
            itemView.getLayoutParams().width = post_width;
        }

        void setData(@NonNull final HotCategory post) {
            GlideUtils.load(mContext, post.getCategoryicon(), cover);
            title.setText(post.getCategoryname());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IntentUtils.startBookListActivity(mContext, Constant.FRAGMENT_CATEGORY_DETAIL, post.getCategoryname(), post.getCategoryid(), post.getSex());
                }
            });
        }
    }
}
