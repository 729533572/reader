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

package com.yn.reader.view.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yn.reader.R;
import com.yn.reader.model.common.Book;
import com.yn.reader.util.ComUtils;
import com.yn.reader.util.GlideUtils;
import com.yn.reader.util.LogUtils;

import me.drakeet.multitype.ItemViewBinder;

/**
 * @author drakeet
 */
public class PostViewBinder extends ItemViewBinder<Book, PostViewBinder.ViewHolder> {
    private int poster_width;
    private Context mContext;

    public PostViewBinder(Context context) {
        mContext = context;
        int screen_width = context.getResources().getDisplayMetrics().widthPixels;
        poster_width = (screen_width-ComUtils.dip2px(16)*5) / 4;
        LogUtils.log("poster_width=" + poster_width);
    }

    @Override
    protected @NonNull
    ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_shelf, parent, false);
        return new ViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Book book) {
        LogUtils.log("position="+getPosition(holder));
        LogUtils.log("position111="+getPosition(holder));
        holder.setData(book);
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private @NonNull
        ImageView cover;
        private @NonNull
        TextView title;
        private @NonNull
        TextView author;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.iv_cover);
            title = itemView.findViewById(R.id.tv_title);
            author = itemView.findViewById(R.id.tv_sub_title);
//            itemView.getLayoutParams().width = poster_width;
//            cover.getLayoutParams().width = poster_width;
        }


        void setData(@NonNull final Book book) {
            GlideUtils.load(mContext, book.getBookimage(), cover);
            title.setText(book.getBookname());
            author.setText(book.getBookauthor());
        }
    }
}
